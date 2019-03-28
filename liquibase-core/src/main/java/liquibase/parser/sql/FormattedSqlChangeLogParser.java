package liquibase.parser.sql;

import liquibase.Scope;
import liquibase.change.core.RawSQLChange;
import liquibase.changelog.ChangeLog;
import liquibase.changelog.ChangeSet;
import liquibase.exception.ChangeLogParseException;
import liquibase.exception.ParseException;
import liquibase.lockservice.DatabaseChangeLogLock;
import liquibase.logging.LogType;
import liquibase.parser.AbstractParser;
import liquibase.parser.ParsedNode;
import liquibase.precondition.core.SqlPrecondition;
import liquibase.util.SqlParser;
import liquibase.util.StreamUtil;
import liquibase.util.StringClauses;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormattedSqlChangeLogParser extends AbstractParser {

    private Pattern changeLogPattern = Pattern.compile("\\-\\-\\s*liquibase formatted.*", Pattern.CASE_INSENSITIVE);
    private Pattern changeSetPattern = Pattern.compile("changeset\\s+([^:]+):\\s*(\\S+).*", Pattern.CASE_INSENSITIVE);
    private Pattern rollbackPattern = Pattern.compile("\\s*\\-\\-[\\s]*rollback (.*)", Pattern.CASE_INSENSITIVE);
    private Pattern preconditionsPattern = Pattern.compile("\\s*\\-\\-[\\s]*preconditions(.*)", Pattern.CASE_INSENSITIVE);
    private Pattern preconditionPattern = Pattern.compile("\\s*\\-\\-[\\s]*precondition\\-([a-zA-Z0-9-]+) (.*)", Pattern.CASE_INSENSITIVE);
    private Pattern stripCommentsPattern = Pattern.compile(".*stripComments:(\\w+).*", Pattern.CASE_INSENSITIVE);
    private Pattern splitStatementsPattern = Pattern.compile(".*splitStatements:(\\w+).*", Pattern.CASE_INSENSITIVE);
    private Pattern rollbackSplitStatementsPattern = Pattern.compile(".*rollbackSplitStatements:(\\w+).*", Pattern.CASE_INSENSITIVE);
    private Pattern endDelimiterPattern = Pattern.compile(".*endDelimiter:(\\S*).*", Pattern.CASE_INSENSITIVE);
    private Pattern rollbackEndDelimiterPattern = Pattern.compile(".*rollbackEndDelimiter:(\\S*).*", Pattern.CASE_INSENSITIVE);
    private Pattern commentPattern = Pattern.compile("\\-\\-[\\s]*comment:? (.*)", Pattern.CASE_INSENSITIVE);
    private Pattern validCheckSumPattern = Pattern.compile("\\-\\-[\\s]*validCheckSum:? (.*)", Pattern.CASE_INSENSITIVE);

    private Pattern runOnChangePattern = Pattern.compile(".*runOnChange:(\\w+).*", Pattern.CASE_INSENSITIVE);
    private Pattern runAlwaysPattern = Pattern.compile(".*runAlways:(\\w+).*", Pattern.CASE_INSENSITIVE);
    private Pattern contextPattern = Pattern.compile(".*context:(\".*\"|\\S*).*", Pattern.CASE_INSENSITIVE);
    private Pattern logicalFilePathPattern = Pattern.compile(".*logicalFilePath:(\\S*).*", Pattern.CASE_INSENSITIVE);
    private Pattern labelsPattern = Pattern.compile(".*labels:(\\S*).*", Pattern.CASE_INSENSITIVE);
    private Pattern runInTransactionPattern = Pattern.compile(".*runInTransaction:(\\w+).*", Pattern.CASE_INSENSITIVE);
    private Pattern dbmsPattern = Pattern.compile(".*dbms:([^,][\\w!,]+).*", Pattern.CASE_INSENSITIVE);
    private Pattern failOnErrorPattern = Pattern.compile(".*failOnError:(\\w+).*", Pattern.CASE_INSENSITIVE);
    private Pattern onFailPattern = Pattern.compile(".*onFail:(\\w+).*", Pattern.CASE_INSENSITIVE);
    private Pattern onErrorPattern = Pattern.compile(".*onError:(\\w+).*", Pattern.CASE_INSENSITIVE);
    private Pattern onUpdateSqlPattern = Pattern.compile(".*onUpdateSQL:(\\w+).*", Pattern.CASE_INSENSITIVE);

    @Override
    public int getPriority(String relativeTo, String path, Class objectType) {
        if (ChangeLog.class.equals(objectType) && path.toLowerCase().endsWith(".sql")) {
            return PRIORITY_DEFAULT;
        }
        return PRIORITY_NOT_APPLICABLE;
    }

//    @Override
//    public boolean supports(String changeLogFile) {
//        BufferedReader reader = null;
//        try {
//            if (changeLogFile.endsWith(".sql")) {
//                InputStream fileStream = openChangeLogFile(changeLogFile);
//                if (fileStream == null) {
//                    return false;
//                }
//                reader = new BufferedReader(StreamUtil.readStreamWithReader(fileStream, null));
//
//                String line = reader.readLine();
//                return (line != null) && line.matches("\\-\\-\\s*liquibase formatted.*");
//            } else {
//                return false;
//            }
//        } catch (IOException e) {
//            Scope.getCurrentScope().getLog(getClass()).fine(LogType.LOG, "Exception reading " + changeLogFile, e);
//            return false;
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    Scope.getCurrentScope().getLog(getClass()).fine(LogType.LOG, "Exception closing " + changeLogFile, e);
//                }
//            }
//        }
//    }


    @Override
    public <ObjectType> ObjectType parse(String relativeTo, String sourcePath, Class<ObjectType> objectType) throws ParseException {
        ChangeLog changeLog = new ChangeLog();


        try {
            StringClauses parsedSql = SqlParser.parse(StreamUtil.readStreamAsString(Scope.getCurrentScope().getResourceAccessor().openStream(relativeTo, sourcePath)), true, true);
            StringClauses.ClauseIterator clauseIterator = parsedSql.getClauseIterator();

            Object firstClause = clauseIterator.next();
            boolean liquibaseFormatted = false;
            if (firstClause instanceof StringClauses.Comment) {
                if (firstClause.toString().matches("(?s)-*liquibase formatted sql.*")) {
                    liquibaseFormatted = true;
                    configureChangeLog((StringClauses.Comment) firstClause, changeLog);
                }
            }

            if (!liquibaseFormatted) {
                RawSQLChange change = new RawSQLChange();
                StringBuilder sql = new StringBuilder();
                sql.append(firstClause.toString());

                while (clauseIterator.hasNext()) {
                    sql.append(clauseIterator.next().toString());
                }
                change.setSql(sql.toString());
                change.setSplitStatements(false);
                change.setStripComments(false);

                ChangeSet changeSet = new ChangeSet();
                changeSet.id = "raw";
                changeSet.author = "sql";
                changeSet.addChange(change);

                changeLog.addChangeSet(changeSet);

                return (ObjectType) changeLog;
            }

            ChangeSet changeSet = null;
            StringBuilder currentSql = new StringBuilder();
            while (clauseIterator.hasNext()) {
                Object clause = clauseIterator.next();
                if (clause instanceof StringClauses.Comment) {
                    String comment = clause.toString();
                    Matcher changeSetPatternMatcher = changeSetPattern.matcher(comment);
                    if (changeSetPatternMatcher.find()) {
                        finishChangeSet(changeSet, currentSql);
                        currentSql = new StringBuilder();

                        changeSet = createChangeSet((StringClauses.Comment) clause, changeLog);
                        changeLog.addChangeSet(changeSet);

                        continue;
                    }
                }

                currentSql.append(clause.toString());
            }

            finishChangeSet(changeSet, currentSql);


//            StringBuffer currentRollbackSql = new StringBuffer();
//
//            ChangeSet changeSet = null;
//            RawSQLChange change = null;
//
//            boolean rollbackSplitStatements = true;
//            String rollbackEndDelimiter = null;
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//
//                Matcher changeLogPatterMatcher = changeLogPattern.matcher(line);
//                if (changeLogPatterMatcher.matches()) {
//                    Matcher logicalFilePathMatcher = logicalFilePathPattern.matcher(line);
//                    changeLog.logicalPath = parseString(logicalFilePathMatcher);
//                }
//
//                Matcher changeSetPatternMatcher = changeSetPattern.matcher(line);
//                if (changeSetPatternMatcher.matches()) {
//                    String finalCurrentSql = changeLogParameters.expandExpressions(StringUtil.trimToNull(currentSql.toString()), changeLog);
//                    if (changeSet != null) {
//
//                        if (finalCurrentSql == null) {
//                            throw new ChangeLogParseException("No SQL for changeset " + changeSet.toString(false));
//                        }
//
//                        change.setSql(finalCurrentSql);
//
//                        if (StringUtil.trimToNull(currentRollbackSql.toString()) != null) {
//                            if (currentRollbackSql.toString().trim().toLowerCase().matches("^not required.*")) {
//                                changeSet.addRollbackChange(new EmptyChange());
//                            } else {
//                                RawSQLChange rollbackChange = new RawSQLChange();
//                                rollbackChange.setSql(changeLogParameters.expandExpressions(currentRollbackSql.toString(), changeLog));
//                                changeSet.addRollbackChange(rollbackChange);
//                            }
//                        }
//                    }
//
//                    Matcher stripCommentsPatternMatcher = stripCommentsPattern.matcher(line);
//                    Matcher splitStatementsPatternMatcher = splitStatementsPattern.matcher(line);
//                    Matcher rollbackSplitStatementsPatternMatcher = rollbackSplitStatementsPattern.matcher(line);
//                    Matcher endDelimiterPatternMatcher = endDelimiterPattern.matcher(line);
//                    Matcher rollbackEndDelimiterPatternMatcher = rollbackEndDelimiterPattern.matcher(line);
//
//                    Matcher logicalFilePathMatcher = logicalFilePathPattern.matcher(line);
//                    Matcher runOnChangePatternMatcher = runOnChangePattern.matcher(line);
//                    Matcher runAlwaysPatternMatcher = runAlwaysPattern.matcher(line);
//                    Matcher contextPatternMatcher = contextPattern.matcher(line);
//                    Matcher labelsPatternMatcher = labelsPattern.matcher(line);
//                    Matcher runInTransactionPatternMatcher = runInTransactionPattern.matcher(line);
//                    Matcher dbmsPatternMatcher = dbmsPattern.matcher(line);
//                    Matcher failOnErrorPatternMatcher = failOnErrorPattern.matcher(line);
//
//                    boolean stripComments = parseBoolean(stripCommentsPatternMatcher, changeSet, true);
//                    boolean splitStatements = parseBoolean(splitStatementsPatternMatcher, changeSet, true);
//                    rollbackSplitStatements = parseBoolean(rollbackSplitStatementsPatternMatcher, changeSet, true);
//                    boolean runOnChange = parseBoolean(runOnChangePatternMatcher, changeSet, false);
//                    boolean runAlways = parseBoolean(runAlwaysPatternMatcher, changeSet, false);
//                    boolean runInTransaction = parseBoolean(runInTransactionPatternMatcher, changeSet, true);
//                    boolean failOnError = parseBoolean(failOnErrorPatternMatcher, changeSet, true);
//
//                    String endDelimiter = parseString(endDelimiterPatternMatcher);
//                    rollbackEndDelimiter = parseString(rollbackEndDelimiterPatternMatcher);
//                    String context = StringUtil.trimToNull(
//                            StringUtil.trimToEmpty(parseString(contextPatternMatcher)).replaceFirst("^\"", "").replaceFirst("\"$", "") //remove surrounding quotes if they're in there
//                    );
//                    String labels = parseString(labelsPatternMatcher);
//                    String logicalFilePath = parseString(logicalFilePathMatcher);
//                    if ((logicalFilePath == null) || "".equals(logicalFilePath)) {
//                        logicalFilePath = changeLog.logicalPath;
//                    }
//                    String dbms = parseString(dbmsPatternMatcher);
//
//
//                    changeSet = new ChangeSet(changeSetPatternMatcher.group(2), changeSetPatternMatcher.group(1), runAlways, runOnChange, logicalFilePath, context, dbms, runInTransaction, changeLog);
//                    changeSet.setLabels(new Labels(labels));
//                    changeSet.setFailOnError(failOnError);
//                    changeLog.addChangeSet(changeSet);
//
//                    change = new RawSQLChange();
//                    change.setSql(finalCurrentSql);
//                    change.setSplitStatements(splitStatements);
//                    change.setStripComments(stripComments);
//                    change.setEndDelimiter(endDelimiter);
//                    changeSet.addChange(change);
//
//                    currentSql = new StringBuffer();
//                    currentRollbackSql = new StringBuffer();
//                } else {
//                    if (changeSet != null) {
//                        Matcher commentMatcher = commentPattern.matcher(line);
//                        Matcher rollbackMatcher = rollbackPattern.matcher(line);
//                        Matcher preconditionsMatcher = preconditionsPattern.matcher(line);
//                        Matcher preconditionMatcher = preconditionPattern.matcher(line);
//                        Matcher validCheckSumMatcher = validCheckSumPattern.matcher(line);
//
//                        if (commentMatcher.matches()) {
//                            if (commentMatcher.groupCount() == 1) {
//                                changeSet.setComments(commentMatcher.group(1));
//                            }
//                        } else if (validCheckSumMatcher.matches()) {
//                            if (validCheckSumMatcher.groupCount() == 1) {
//                                changeSet.addValidCheckSum(validCheckSumMatcher.group(1));
//                            }
//                        } else if (rollbackMatcher.matches()) {
//                            if (rollbackMatcher.groupCount() == 1) {
//                                currentRollbackSql.append(rollbackMatcher.group(1)).append(System.lineSeparator());
//                            }
//                        } else if (preconditionsMatcher.matches()) {
//                            if (preconditionsMatcher.groupCount() == 1) {
//                                String body = preconditionsMatcher.group(1);
//                                Matcher onFailMatcher = onFailPattern.matcher(body);
//                                Matcher onErrorMatcher = onErrorPattern.matcher(body);
//                                Matcher onUpdateSqlMatcher = onUpdateSqlPattern.matcher(body);
//
//                                Preconditions pc = new Preconditions();
//                                pc.onFail = Preconditions.FailOption.valueOf(StringUtil.trimToNull(parseString(onFailMatcher)));
//                                pc.onError = Preconditions.ErrorOption.valueOf(StringUtil.trimToNull(parseString(onErrorMatcher)));
//                                pc.onSqlOutput = Preconditions.OnSqlOutputOption.valueOf(StringUtil.trimToNull(parseString(onUpdateSqlMatcher)));
//                                changeSet.setPreconditions(pc);
//                            }
//                        } else if (preconditionMatcher.matches()) {
//                            if (changeSet.getPreconditions() == null) {
//                                // create the defaults
//                                changeSet.setPreconditions(new Preconditions());
//                            }
//                            if (preconditionMatcher.groupCount() == 2) {
//                                String name = StringUtil.trimToNull(preconditionMatcher.group(1));
//                                if (name != null) {
//                                    String body = preconditionMatcher.group(2).trim();
//                                    if ("sql-check".equals(name)) {
//                                        changeSet.getPreconditions().add(parseSqlCheckCondition(changeLogParameters.expandExpressions(StringUtil.trimToNull(body), changeSet.getChangeLog())));
//                                    } else {
//                                        throw new ChangeLogParseException("The '" + name + "' precondition type is not supported.");
//                                    }
//                                }
//                            }
//                        } else {
//                            currentSql.append(line).append(System.lineSeparator());
//                        }
//                    }
//                }
//            }

//            if (changeSet != null) {
//                change.setSql(changeLogParameters.expandExpressions(StringUtil.trimToNull(currentSql.toString()), changeSet.getChangeLog()));
//
//                if ((change.getEndDelimiter() == null) && StringUtil.trimToEmpty(change.getSql()).endsWith("\n/")) {
//                    change.setEndDelimiter("\n/$");
//                }
//
//                if (StringUtil.trimToNull(currentRollbackSql.toString()) != null) {
//                    if (currentRollbackSql.toString().trim().toLowerCase().matches("^not required.*")) {
//                        changeSet.addRollbackChange(new EmptyChange());
//                    } else {
//                        RawSQLChange rollbackChange = new RawSQLChange();
//                        rollbackChange.setSql(changeLogParameters.expandExpressions(currentRollbackSql.toString(), changeSet.getChangeLog()));
//                        rollbackChange.setSplitStatements(rollbackSplitStatements);
//                        if (rollbackEndDelimiter != null) {
//                            rollbackChange.setEndDelimiter(rollbackEndDelimiter);
//                        }
//                        changeSet.addRollbackChange(rollbackChange);
//                    }
//                }
//            }

        } catch (IOException e) {
            throw new ParseException("Error opening " + sourcePath + " " + e.getMessage(), e, null);
        }
        return (ObjectType) changeLog;
    }

    private void finishChangeSet(ChangeSet changeSet, StringBuilder currentSql) {
        if (changeSet == null) {
            return;
        }

        RawSQLChange sqlChange = new RawSQLChange();
        sqlChange.setSql(currentSql.toString());

        changeSet.addChange(sqlChange);
    }

    protected void configureChangeLog(StringClauses.Comment changeLogComment, ChangeLog changeLog) {

    }

    protected ChangeSet createChangeSet(StringClauses.Comment changeSetComment, ChangeLog changeLog) {
        ChangeSet changeSet;
        changeSet = new ChangeSet(changeLog);

        return changeSet;
    }

    private SqlPrecondition parseSqlCheckCondition(String body) throws ChangeLogParseException {
        Pattern[] patterns = new Pattern[]{
                Pattern.compile("^(?:expectedResult:)?(\\w+) (.*)", Pattern.CASE_INSENSITIVE),
                Pattern.compile("^(?:expectedResult:)?'([^']+)' (.*)", Pattern.CASE_INSENSITIVE),
                Pattern.compile("^(?:expectedResult:)?\"([^\"]+)\" (.*)", Pattern.CASE_INSENSITIVE)
        };
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(body);
            if (matcher.matches() && (matcher.groupCount() == 2)) {
                SqlPrecondition p = new SqlPrecondition();
                p.expectedResult = matcher.group(1);
                p.sql = matcher.group(2);
                return p;
            }
        }
        throw new ChangeLogParseException("Could not parse a SqlCheck precondition from '" + body + "'.");
    }


    private String parseString(Matcher matcher) {
        String endDelimiter = null;
        if (matcher.matches()) {
            endDelimiter = matcher.group(1);
        }
        return endDelimiter;
    }

    private boolean parseBoolean(Matcher matcher, ChangeSet changeSet, boolean defaultValue) throws ChangeLogParseException {
        boolean stripComments = defaultValue;
        if (matcher.matches()) {
            try {
                stripComments = Boolean.parseBoolean(matcher.group(1));
            } catch (Exception e) {
                throw new ChangeLogParseException("Cannot parse " + changeSet + " " + matcher.toString().replaceAll("\\.*", "") + " as a boolean");
            }
        }
        return stripComments;
    }

    protected InputStream openChangeLogFile(String physicalChangeLogLocation) throws IOException {
        InputStream resourceAsStream = Scope.getCurrentScope().getResourceAccessor().openStream(null, physicalChangeLogLocation);
        if (resourceAsStream == null) {
            final File physicalChangeLogFile = new File(physicalChangeLogLocation);
            throw new IOException("File does not exist: " + physicalChangeLogFile.getAbsolutePath());
        }
        return resourceAsStream;
    }

    @Override
    public String describeOriginal(ParsedNode parsedNode) {
        return "TODO";
    }
}
