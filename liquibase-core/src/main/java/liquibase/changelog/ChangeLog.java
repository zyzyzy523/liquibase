package liquibase.changelog;

import liquibase.*;
import liquibase.database.Database;
import liquibase.exception.LiquibaseException;
import liquibase.exception.ParseException;
import liquibase.exception.SetupException;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.parser.ParsedNode;
import liquibase.precondition.Preconditions;
import liquibase.resource.ResourceAccessor;

import java.util.*;

/**
 * Encapsulates the information stored in the change log XML file.
 */
public class ChangeLog extends AbstractExtensibleObject implements ChangeLogEntry {

    /**
     * The physical path from which the changelog was actually loaded. Used for reporting and finding relative files.
     */
    public String physicalPath;

    /**
     * The path used in 'path' section of a changeSet's id+author+path identifier. If null, the {@link #physicalPath} will be used.
     */
    public String logicalPath;

    /**
     * The default {@link liquibase.database.Database.QuotingStrategy} to use for actions objects in this changeLog.
     */
    public Database.QuotingStrategy objectQuotingStrategy;

    public List<ChangeLogEntry> items = new ArrayList<>();

    public ChangeLog containerChangeLog;

    public ChangeLog() {
    }

    public ChangeLog(String physicalPath) {
        this.physicalPath = physicalPath;
    }

    @Override
    public ChangeLog getContainerChangeLog() {
        return containerChangeLog;
    }

    public List<ChangeSet> getChangeSets() {
        List<ChangeSet> returnList = new ArrayList<>();
        for (ChangeLogEntry entry : items) {
            if (entry instanceof ChangeSet) {
                returnList.add((ChangeSet) entry);
            }
        }
        return Collections.unmodifiableList(returnList);
    }

    /**
     * Returns the {@link #logicalPath}, or if not set then returns the {@link #physicalPath}
     */
    public String getPath() {
        if (logicalPath != null) {
            return logicalPath;
        }
        return physicalPath;
    }

    public RuntimeEnvironment getRuntimeEnvironment() {
        return null;
//        return runtimeEnvironment;
    }

    public void setRuntimeEnvironment(RuntimeEnvironment runtimeEnvironment) {
//        this.runtimeEnvironment = runtimeEnvironment;
    }


    //    @Override
    public Preconditions getPreconditions() {
        return null; // return preconditionContainer;
    }
//
//    @Override
    public void setPreconditions(Preconditions precond) {
//        if (precond == null) {
//            this.preconditionContainer = new PreconditionContainer();
//        } else {
//            preconditionContainer = precond;
//        }
    }
//
//
    public ChangeLogParameters getChangeLogParameters() {
        return null;
//        return changeLogParameters;
    }

    public void setChangeLogParameters(ChangeLogParameters changeLogParameters) {
//        this.changeLogParameters = changeLogParameters;
    }



    public ContextExpression getContexts() {
        return null;
//        return contexts;
    }

    public void setContexts(ContextExpression contexts) {
//        this.contexts = contexts;
    }

    public ContextExpression getIncludeContexts() {
//        return includeContexts;
        return null;
    }

    public void setIncludeLabels(LabelExpression labels) {
//        this.includeLabels = labels;
    }

    public LabelExpression getIncludeLabels() {
        return null;
//        return includeLabels;
    }

    public void setIncludeIgnore(boolean ignore) {
//        this.includeIgnore = ignore;
    }

    public boolean isIncludeIgnore() {
        return false;
//        return this.includeIgnore;
    }

    public void setIncludeContexts(ContextExpression includeContexts) {
//        this.includeContexts = includeContexts;
    }


    public ChangeSet getChangeSet(String path, String author, String id) {
//        for (ChangeSet changeSet : changeSets) {
//            if (normalizePath(changeSet.getFilePath()).equalsIgnoreCase(normalizePath(path))
//                    && changeSet.getAuthor().equalsIgnoreCase(author)
//                    && changeSet.getId().equalsIgnoreCase(id)
//                    && isDbmsMatch(changeSet.getDbmsSet())) {
//                return changeSet;
//            }
//        }
//
        return null;
    }

    public void addChangeSet(ChangeSet changeSet) {
//        if (changeSet.getRunOrder() == null) {
//            ListIterator<ChangeLogEntry> it = this.items.listIterator(this.items.size());
//            boolean added = false;
//            while (it.hasPrevious() && !added) {
//                if (!"last".equals(it.previous().getRunOrder())) {
//                    it.next();
//                    it.add(changeSet);
//                    added = true;
//                }
//            }
//            if (!added) {
//                it.add(changeSet);
//            }
//
//        } else if ("first".equals(changeSet.getRunOrder())) {
//            ListIterator<ChangeSet> it = this.changeSets.listIterator();
//            boolean added = false;
//            while (it.hasNext() && !added) {
//                if (!"first".equals(it.next().getRunOrder())) {
//                    it.previous();
//                    it.add(changeSet);
//                    added = true;
//                }
//            }
//            if (!added) {
//                this.changeSets.add(changeSet);
//            }
//        } else if ("last".equals(changeSet.getRunOrder())) {
//            this.changeSets.add(changeSet);
//        } else {
//            throw new UnexpectedLiquibaseException("Unknown runOrder: " + changeSet.getRunOrder());
//        }
        this.items.add(changeSet);
    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if ((o == null) || (getClass() != o.getClass())) {
//            return false;
//        }
//
//        ChangeLog that = (ChangeLog) o;
//
//        return getFilePath().equals(that.getFilePath());
//
//    }
//
//    @Override
//    public int hashCode() {
//        return getFilePath().hashCode();
//    }

    public void validate(Database database, String... contexts) throws LiquibaseException {
//        this.validate(database, new Contexts(contexts), new LabelExpression());
    }
//
    public void validate(Database database, Contexts contexts, LabelExpression labelExpression)
            throws LiquibaseException {
//
//        database.setObjectQuotingStrategy(objectQuotingStrategy);
//
//        ChangeLogIterator logIterator = new ChangeLogIterator(
//                this,
//                new DbmsChangeSetFilter(database),
//                new ContextChangeSetFilter(contexts),
//                new LabelChangeSetFilter(labelExpression)
//        );
//
//        ValidatingVisitor validatingVisitor = new ValidatingVisitor(database.getRanChangeSetList());
//        validatingVisitor.validate(database, this);
//        logIterator.run(validatingVisitor, new RuntimeEnvironment(database, contexts, labelExpression));
//
//        for (String message : validatingVisitor.getWarnings().getMessages()) {
//            Scope.getCurrentScope().getLog(getClass()).warning(LogType.LOG, message);
//        }
//
//        if (!validatingVisitor.validationPassed()) {
//            throw new ValidationFailedException(validatingVisitor);
//        }
    }
//
    public ChangeSet getChangeSet(RanChangeSet ranChangeSet) {
//        return getChangeSet(ranChangeSet.getChangeLog(), ranChangeSet.getAuthor(), ranChangeSet.getId());
        return null;
    }
//
    public void load(ParsedNode parsedNode)
            throws ParseException, SetupException {
//        setLogicalFilePath(parsedNode.getChildValue(null, "logicalFilePath", String.class));
//        setContexts(new ContextExpression(parsedNode.getChildValue(null, "context", String.class)));
//        String objectQuotingStrategy = parsedNode.getChildValue(null, "objectQuotingStrategy", String.class);
//        if (objectQuotingStrategy != null) {
//            setObjectQuotingStrategy(ObjectQuotingStrategy.valueOf(objectQuotingStrategy));
//        }
//        for (ParsedNode childNode : parsedNode.getChildren()) {
//            handleChildNode(childNode, resourceAccessor);
//        }
    }
//
    protected void expandExpressions(ParsedNode parsedNode) {
//        if (changeLogParameters == null) {
//            return;
//        }
//        try {
//            Object value = parsedNode.getValue();
//            if ((value != null) && (value instanceof String)) {
//                parsedNode.setValue(changeLogParameters.expandExpressions(parsedNode.getValue(String.class), this));
//            }
//
//            List<ParsedNode> children = parsedNode.getChildren();
//            if (children != null) {
//                for (ParsedNode child : children) {
//                    expandExpressions(child);
//                }
//            }
//        } catch (ParsedNodeException e) {
//            throw new UnexpectedLiquibaseException(e);
//        }
    }
//
    protected void handleChildNode(ParsedNode node, ResourceAccessor resourceAccessor)
            throws ParseException, SetupException {
//        expandExpressions(node);
//        String nodeName = node.getName();
//        switch (nodeName) {
//            case "changeSet":
//                if (isDbmsMatch(node.getChildValue(null, "dbms", String.class))) {
//                    this.addChangeSet(createChangeSet(node, resourceAccessor));
//                }
//                break;
//            case "include": {
//                String path = node.getChildValue(null, "file", String.class);
//                if (path == null) {
//                    throw new UnexpectedLiquibaseException("No 'file' attribute on 'include'");
//                }
//                path = path.replace('\\', '/');
//                ContextExpression includeContexts = new ContextExpression(node.getChildValue(null, "context", String.class));
//                LabelExpression labelExpression = new LabelExpression(node.getChildValue(null, "labels", String.class));
//                Boolean ignore = node.getChildValue(null, "ignore", Boolean.class);
//                try {
//                    include(path,
//                            node.getChildValue(null, "relativeToChangelogFile", false),
//                            resourceAccessor,
//                            includeContexts,
//                            labelExpression,
//                            ignore);
//                } catch (LiquibaseException e) {
//                    throw new SetupException(e);
//                }
//                break;
//            }
//            case "includeAll": {
//                String path = node.getChildValue(null, "path", String.class);
//                String resourceFilterDef = node.getChildValue(null, "filter", String.class);
//                if (resourceFilterDef == null) {
//                    resourceFilterDef = node.getChildValue(null, "resourceFilter", String.class);
//                }
//                IncludeAllFilter resourceFilter = null;
//                if (resourceFilterDef != null) {
//                    try {
//                        resourceFilter = (IncludeAllFilter) Class.forName(resourceFilterDef).getConstructor().newInstance();
//                    } catch (ReflectiveOperationException e) {
//                        throw new SetupException(e);
//                    }
//                }
//
//                String resourceComparatorDef = node.getChildValue(null, "resourceComparator", String.class);
//                Comparator<String> resourceComparator = null;
//                if (resourceComparatorDef != null) {
//                    try {
//                        resourceComparator = (Comparator<String>) Class.forName(resourceComparatorDef).getConstructor().newInstance();
//                    } catch (ReflectiveOperationException e) {
//                        //take default comparator
//                        Scope.getCurrentScope().getLog(getClass()).info(LogType.LOG, "no resourceComparator defined - taking default " +
//                                "implementation");
//                        resourceComparator = getStandardChangeLogComparator();
//                    }
//                }
//
//                ContextExpression includeContexts = new ContextExpression(node.getChildValue(null, "context", String.class));
//                LabelExpression labelExpression = new LabelExpression(node.getChildValue(null, "labels", String.class));
//                if (labelExpression == null) {
//                    labelExpression = new LabelExpression();
//                }
//                Boolean ignore = node.getChildValue(null, "ignore", Boolean.class);
//                if (ignore == null) {
//                    ignore = false;
//                }
//                includeAll(path, node.getChildValue(null, "relativeToChangelogFile", false), resourceFilter,
//                        node.getChildValue(null, "errorIfMissingOrEmpty", true),
//                        resourceComparator,
//                        resourceAccessor,
//                        includeContexts,
//                        labelExpression,
//                        ignore);
//                break;
//            }
//            case "preConditions": {
//                this.preconditionContainer = new PreconditionContainer();
//                try {
//                    this.preconditionContainer.load(node, resourceAccessor);
//                } catch (ParsedNodeException e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//            case "property": {
//                try {
//                    String context = node.getChildValue(null, "context", String.class);
//                    String dbms = node.getChildValue(null, "dbms", String.class);
//                    String labels = node.getChildValue(null, "labels", String.class);
//                    Boolean global = node.getChildValue(null, "global", Boolean.class);
//                    if (global == null) {
//                        // okay behave like liquibase < 3.4 and set global == true
//                        global = true;
//                    }
//
//                    String file = node.getChildValue(null, "file", String.class);
//
//                    if (file == null) {
//                        // direct referenced property, no file
//                        String name = node.getChildValue(null, "name", String.class);
//                        String value = node.getChildValue(null, "value", String.class);
//
//                        this.changeLogParameters.set(name, value, context, labels, dbms, global, this);
//                    } else {
//                        // read properties from the file
//                        Properties props = new Properties();
//                        InputStream propertiesStream = resourceAccessor.openStream(null, file);
//                        if (propertiesStream == null) {
//                            Scope.getCurrentScope().getLog(getClass()).info(LogType.LOG, "Could not open properties file " + file);
//                        } else {
//                            props.load(propertiesStream);
//
//                            for (Map.Entry entry : props.entrySet()) {
//                                this.changeLogParameters.set(
//                                        entry.getKey().toString(),
//                                        entry.getValue().toString(),
//                                        context,
//                                        labels,
//                                        dbms,
//                                        global,
//                                        this
//                                );
//                            }
//                        }
//                    }
//                } catch (IOException e) {
//                    throw new ParsedNodeException(e);
//                }
//
//                break;
//            }
//        }
    }
//
    public boolean isDbmsMatch(String dbmsList) {
//        return isDbmsMatch(DatabaseList.toDbmsSet(dbmsList));
        return true;
    }
//
    public boolean isDbmsMatch(Set<String> dbmsSet) {
//        return dbmsSet == null
//                || changeLogParameters == null
//                || changeLogParameters.getValue("database.typeName", this) == null
//                || DatabaseList.definitionMatches(dbmsSet, changeLogParameters.getValue("database.typeName", this).toString(), true);
        return true;
    }
//
    public void includeAll(String pathName,
                           boolean isRelativeToChangelogFile,
                           IncludeAllFilter resourceFilter,
                           boolean errorIfMissingOrEmpty,
                           Comparator<String> resourceComparator,
                           ResourceAccessor resourceAccessor,
                           ContextExpression includeContexts,
                           LabelExpression labelExpression,
                           boolean ignore)
            throws SetupException {
//        try {
//            if (pathName == null) {
//                throw new SetupException("No path attribute for includeAll");
//            }
//            pathName = pathName.replace('\\', '/');
//
//            if (!(pathName.endsWith("/"))) {
//                pathName = pathName + '/';
//            }
//            LOG.fine(LogType.LOG, "includeAll for " + pathName);
//            LOG.fine(LogType.LOG, "Using file opener for includeAll: " + resourceAccessor.toString());
//
//            String relativeTo = null;
//            if (isRelativeToChangelogFile) {
//                relativeTo = this.getPhysicalFilePath();
//            }
//
//            Set<String> unsortedResources = null;
//            try {
//                unsortedResources = resourceAccessor.list(relativeTo, pathName, true, true, false);
//            } catch (IOException e) {
//                if (errorIfMissingOrEmpty) {
//                    throw e;
//                }
//            }
//            SortedSet<String> resources = new TreeSet<>(resourceComparator);
//            if (unsortedResources != null) {
//                for (String resourcePath : unsortedResources) {
//                    if ((resourceFilter == null) || resourceFilter.include(resourcePath)) {
//                        resources.add(resourcePath);
//                    }
//                }
//            }
//
//            if (resources.isEmpty() && errorIfMissingOrEmpty) {
//                throw new SetupException(
//                        "Could not find directory or directory was empty for includeAll '" + pathName + "'");
//            }
//
//            for (String path : resources) {
//                Scope.getCurrentScope().getLog(getClass()).info(LogType.LOG, "Reading resource: " + path);
//                include(path, false, resourceAccessor, includeContexts, labelExpression, ignore);
//            }
//        } catch (Exception e) {
//            throw new SetupException(e);
//        }
    }
//
    public boolean include(String fileName,
                           boolean isRelativePath,
                           ResourceAccessor resourceAccessor,
                           ContextExpression includeContexts,
                           LabelExpression labelExpression,
                           Boolean ignore)
            throws LiquibaseException {
//
//        if (".svn".equalsIgnoreCase(fileName) || "cvs".equalsIgnoreCase(fileName)) {
//            return false;
//        }
//
//        String relativeBaseFileName = this.getPhysicalFilePath();
//        if (isRelativePath) {
//            // workaround for FilenameUtils.normalize() returning null for relative paths like ../conf/liquibase.xml
//            String tempFile = FilenameUtils.concat(FilenameUtils.getFullPath(relativeBaseFileName), fileName);
//            if (tempFile != null && new File(tempFile).exists() == true) {
//                fileName = tempFile;
//            } else {
//                fileName = FilenameUtils.getFullPath(relativeBaseFileName) + fileName;
//            }
//        }
//        ChangeLog changeLog;
//        try {
//            ChangeLog rootChangeLog = ROOT_CHANGE_LOG.get();
//            if (rootChangeLog == null) {
//                ROOT_CHANGE_LOG.set(this);
//            }
//            ChangeLog parentChangeLog = PARENT_CHANGE_LOG.get();
//            PARENT_CHANGE_LOG.set(this);
//            try {
//                ChangeLogParser parser = ChangeLogParserFactory.getInstance().getParser(fileName, resourceAccessor);
//                changeLog = parser.parse(fileName, changeLogParameters, resourceAccessor);
//                changeLog.setIncludeContexts(includeContexts);
//                changeLog.setIncludeLabels(labelExpression);
//                changeLog.setIncludeIgnore(ignore != null ? ignore.booleanValue() : false);
//            } finally {
//                if (rootChangeLog == null) {
//                    ROOT_CHANGE_LOG.remove();
//                }
//                if (parentChangeLog == null) {
//                    PARENT_CHANGE_LOG.remove();
//                } else {
//                    PARENT_CHANGE_LOG.set(parentChangeLog);
//                }
//            }
//        } catch (UnknownChangelogFormatException e) {
//            Scope.getCurrentScope().getLog(getClass()).warning(
//                    LogType.LOG, "included file " + relativeBaseFileName + "/" + fileName + " is not a recognized file type"
//            );
//            return false;
//        }
//        PreconditionContainer preconditions = changeLog.getPreconditions();
//        if (preconditions != null) {
//            if (null == this.getPreconditions()) {
//                this.setPreconditions(new PreconditionContainer());
//            }
//            this.getPreconditions().addNestedPrecondition(preconditions);
//        }
//        for (ChangeSet changeSet : changeLog.getChangeSets()) {
//            addChangeSet(changeSet);
//        }
//
        return true;
    }
//
    protected ChangeSet createChangeSet(ParsedNode node, ResourceAccessor resourceAccessor) throws ParseException {
//        ChangeSet changeSet = new ChangeSet(this);
//        changeSet.setChangeLogParameters(this.getChangeLogParameters());
//        changeSet.load(node, resourceAccessor);
//        return changeSet;
        return null;
    }
//
    protected Comparator<String> getStandardChangeLogComparator() {
        return new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                //by ignoring WEB-INF/classes in path all changelog Files independent
                //whehther they are in a WAR or in a JAR are order following the same rule
                return o1.replace("WEB-INF/classes/", "").compareTo(o2.replace("WEB-INF/classes/", ""));
            }
        };
    }

    public void setIgnoreClasspathPrefix(boolean ignoreClasspathPrefix) {
//        this.ignoreClasspathPrefix = ignoreClasspathPrefix;
    }
//
    public boolean ignoreClasspathPrefix() {
//        return ignoreClasspathPrefix;
        return true;
    }

    protected String normalizePath(String filePath) {
//        if (ignoreClasspathPrefix) {
//            return filePath.replaceFirst("^classpath:", "");
//        }
        return filePath;
    }
//
    public void clearCheckSums() {
//        for (ChangeSet changeSet : getChangeSets()) {
//            changeSet.clearCheckSum();
//        }
    }

}
