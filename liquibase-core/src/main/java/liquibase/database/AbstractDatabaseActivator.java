package liquibase.database;

import liquibase.CatalogAndSchema;
import liquibase.Scope;
import liquibase.change.Change;
import liquibase.changelog.ChangeLog;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.RanChangeSet;
import liquibase.exception.ValidationErrors;
import liquibase.servicelocator.AbstractServiceActivator;
import liquibase.servicelocator.ServiceActivator;
import liquibase.sql.visitor.SqlVisitor;
import liquibase.statement.DatabaseFunction;
import liquibase.statement.SqlStatement;
import liquibase.structure.DatabaseObject;

import java.io.Writer;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public abstract class AbstractDatabaseActivator<ClassToActivate> extends AbstractServiceActivator<ClassToActivate> implements Database {

    @Override
    public int getPriority() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) {
        throw new NoSuchMethodError();
    }

    @Override
    public String getDefaultDriver(String url) {
        throw new NoSuchMethodError();
    }

    @Override
    public DatabaseConnection getConnection() {
        throw new NoSuchMethodError();
    }

    @Override
    public void setConnection(DatabaseConnection conn) {
        throw new NoSuchMethodError();

    }

    @Override
    public boolean requiresUsername() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean requiresPassword() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean getAutoCommitMode() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean supportsDDLInTransaction() {
        throw new NoSuchMethodError();
    }

    @Override
    public String getDatabaseProductName() {
        throw new NoSuchMethodError();
    }

    @Override
    public String getDatabaseProductVersion() {
        throw new NoSuchMethodError();
    }

    @Override
    public int getDatabaseMajorVersion() {
        throw new NoSuchMethodError();
    }

    @Override
    public int getDatabaseMinorVersion() {
        throw new NoSuchMethodError();
    }

    @Override
    public String getShortName() {
        throw new NoSuchMethodError();
    }

    @Override
    public String getDefaultCatalogName() {
        throw new NoSuchMethodError();
    }

    @Override
    public void setDefaultCatalogName(String catalogName) {
        throw new NoSuchMethodError();

    }

    @Override
    public String getDefaultSchemaName() {
        throw new NoSuchMethodError();
    }

    @Override
    public Integer getDefaultScaleForNativeDataType(String nativeDataType) {
        throw new NoSuchMethodError();
    }

    @Override
    public void setDefaultSchemaName(String schemaName) {
        throw new NoSuchMethodError();
    }

    @Override
    public Integer getDefaultPort() {
        throw new NoSuchMethodError();
    }

    @Override
    public Integer getFetchSize() {
        throw new NoSuchMethodError();
    }

    @Override
    public String getLiquibaseCatalogName() {
        throw new NoSuchMethodError();
    }

    @Override
    public void setLiquibaseCatalogName(String catalogName) {
        throw new NoSuchMethodError();
    }

    @Override
    public String getLiquibaseSchemaName() {
        throw new NoSuchMethodError();
    }

    @Override
    public void setLiquibaseSchemaName(String schemaName) {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean supportsInitiallyDeferrableColumns() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean supportsSequences() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean supportsDropTableCascadeConstraints() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean supportsAutoIncrement() {
        throw new NoSuchMethodError();
    }

    @Override
    public String getDateLiteral(String isoDate) {
        throw new NoSuchMethodError();
    }

    @Override
    public String getCurrentDateTimeFunction() {
        throw new NoSuchMethodError();
    }

    @Override
    public void setCurrentDateTimeFunction(String function) {
        throw new NoSuchMethodError();
    }

    @Override
    public String getLineComment() {
        throw new NoSuchMethodError();
    }

    @Override
    public String getAutoIncrementClause(BigInteger startWith, BigInteger incrementBy, String generationType, Boolean defaultOnNull) {
        throw new NoSuchMethodError();
    }

    @Override
    public String getDatabaseChangeLogTableName() {
        throw new NoSuchMethodError();
    }

    @Override
    public void setDatabaseChangeLogTableName(String tableName) {
        throw new NoSuchMethodError();
    }

    @Override
    public String getDatabaseChangeLogLockTableName() {
        throw new NoSuchMethodError();
    }

    @Override
    public void setDatabaseChangeLogLockTableName(String tableName) {
        throw new NoSuchMethodError();
    }

    @Override
    public String getLiquibaseTablespaceName() {
        throw new NoSuchMethodError();
    }

    @Override
    public void setLiquibaseTablespaceName(String tablespaceName) {
        throw new NoSuchMethodError();
    }

    @Override
    public String getConcatSql(String... values) {
        throw new NoSuchMethodError();
    }

    @Override
    public void setCanCacheLiquibaseTableInfo(boolean canCacheLiquibaseTableInfo) {
        throw new NoSuchMethodError();

    }

    @Override
    public void dropDatabaseObjects(CatalogAndSchema schema) {
        throw new NoSuchMethodError();

    }

    @Override
    public void tag(String tagString) {
        throw new NoSuchMethodError();

    }

    @Override
    public boolean doesTagExist(String tag) {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean isSystemObject(DatabaseObject example) {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean isLiquibaseObject(DatabaseObject object) {
        throw new NoSuchMethodError();
    }

    @Override
    public String getViewDefinition(CatalogAndSchema schema, String name) {
        throw new NoSuchMethodError();
    }

    @Override
    public String getDateLiteral(Date date) {
        throw new NoSuchMethodError();
    }

    @Override
    public String getTimeLiteral(Time time) {
        throw new NoSuchMethodError();
    }

    @Override
    public String getDateTimeLiteral(Timestamp timeStamp) {
        throw new NoSuchMethodError();
    }

    @Override
    public String getDateLiteral(java.util.Date defaultDateValue) {
        throw new NoSuchMethodError();
    }

    @Override
    public String escapeObjectName(String catalogName, String schemaName, String objectName, Class<? extends DatabaseObject> objectType) {
        throw new NoSuchMethodError();
    }

    @Override
    public String escapeTableName(String catalogName, String schemaName, String tableName) {
        throw new NoSuchMethodError();
    }

    @Override
    public String escapeIndexName(String catalogName, String schemaName, String indexName) {
        throw new NoSuchMethodError();
    }

    @Override
    public String escapeObjectName(String objectName, Class<? extends DatabaseObject> objectType) {
        throw new NoSuchMethodError();
    }

    @Override
    public int getMaxFractionalDigitsForTimestamp() {
        throw new NoSuchMethodError();
    }

    @Override
    public int getDefaultFractionalDigitsForTimestamp() {
        throw new NoSuchMethodError();
    }

    @Override
    public String escapeColumnName(String catalogName, String schemaName, String tableName, String columnName) {
        throw new NoSuchMethodError();
    }

    @Override
    public String escapeColumnName(String catalogName, String schemaName, String tableName, String columnName, boolean quoteNamesThatMayBeFunctions) {
        throw new NoSuchMethodError();
    }

    @Override
    public String escapeColumnNameList(String columnNames) {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean supportsTablespaces() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean supportsCatalogs() {
        throw new NoSuchMethodError();
    }

    @Override
    public CatalogAndSchema.CatalogAndSchemaCase getSchemaAndCatalogCase() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean supportsSchemas() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean supportsCatalogInObjectName(Class<? extends DatabaseObject> type) {
        throw new NoSuchMethodError();
    }

    @Override
    public String generatePrimaryKeyName(String tableName) {
        throw new NoSuchMethodError();
    }

    @Override
    public String escapeSequenceName(String catalogName, String schemaName, String sequenceName) {
        throw new NoSuchMethodError();
    }

    @Override
    public String escapeViewName(String catalogName, String schemaName, String viewName) {
        throw new NoSuchMethodError();
    }

    @Override
    public ChangeSet.RunStatus getRunStatus(ChangeSet changeSet) {
        throw new NoSuchMethodError();
    }

    @Override
    public RanChangeSet getRanChangeSet(ChangeSet changeSet) {
        throw new NoSuchMethodError();
    }

    @Override
    public void markChangeSetExecStatus(ChangeSet changeSet, ChangeSet.ExecType execType) {
        throw new NoSuchMethodError();
    }

    @Override
    public List<RanChangeSet> getRanChangeSetList() {
        throw new NoSuchMethodError();
    }

    @Override
    public java.util.Date getRanDate(ChangeSet changeSet) {
        throw new NoSuchMethodError();
    }

    @Override
    public void removeRanStatus(ChangeSet changeSet) {
        throw new NoSuchMethodError();
    }

    @Override
    public void commit() {
        throw new NoSuchMethodError();
    }

    @Override
    public void rollback() {
        throw new NoSuchMethodError();
    }

    @Override
    public String escapeStringForDatabase(String string) {
        throw new NoSuchMethodError();
    }

    @Override
    public void close() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean supportsRestrictForeignKeys() {
        throw new NoSuchMethodError();
    }

    @Override
    public String escapeConstraintName(String constraintName) {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean isAutoCommit() {
        throw new NoSuchMethodError();
    }

    @Override
    public void setAutoCommit(boolean b) {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean isSafeToRunUpdate() {
        throw new NoSuchMethodError();
    }

    @Override
    public void executeStatements(Change change, ChangeLog changeLog, List<SqlVisitor> sqlVisitors) {
        throw new NoSuchMethodError();
    }

    @Override
    public void execute(SqlStatement[] statements, List<SqlVisitor> sqlVisitors) {
        throw new NoSuchMethodError();
    }

    @Override
    public void saveStatements(Change change, List<SqlVisitor> sqlVisitors, Writer writer) {
        throw new NoSuchMethodError();
    }

    @Override
    public void executeRollbackStatements(Change change, List<SqlVisitor> sqlVisitors) {
        throw new NoSuchMethodError();
    }

    @Override
    public void executeRollbackStatements(SqlStatement[] statements, List<SqlVisitor> sqlVisitors) {
        throw new NoSuchMethodError();
    }

    @Override
    public void saveRollbackStatement(Change change, List<SqlVisitor> sqlVisitors, Writer writer) {
        throw new NoSuchMethodError();
    }

    @Override
    public java.util.Date parseDate(String dateAsString) {
        throw new NoSuchMethodError();
    }

    @Override
    public List<DatabaseFunction> getDateFunctions() {
        throw new NoSuchMethodError();
    }

    @Override
    public void resetInternalState() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean supportsForeignKeyDisable() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean disableForeignKeyChecks() {
        throw new NoSuchMethodError();
    }

    @Override
    public void enableForeignKeyChecks() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean isCaseSensitive() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean isReservedWord(String string) {
        throw new NoSuchMethodError();
    }

    @Override
    public CatalogAndSchema correctSchema(CatalogAndSchema schema) {
        throw new NoSuchMethodError();
    }

    @Override
    public String correctObjectName(String name, Class<? extends DatabaseObject> objectType) {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean isFunction(String string) {
        throw new NoSuchMethodError();
    }

    @Override
    public int getDataTypeMaxParameters(String dataTypeName) {
        throw new NoSuchMethodError();
    }

    @Override
    public CatalogAndSchema getDefaultSchema() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean dataTypeIsNotModifiable(String typeName) {
        throw new NoSuchMethodError();
    }

    @Override
    public String generateDatabaseFunctionValue(DatabaseFunction databaseFunction) {
        throw new NoSuchMethodError();
    }

    @Override
    public ObjectQuotingStrategy getObjectQuotingStrategy() {
        throw new NoSuchMethodError();
    }

    @Override
    public void setObjectQuotingStrategy(ObjectQuotingStrategy quotingStrategy) {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean createsIndexesForForeignKeys() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean getOutputDefaultSchema() {
        throw new NoSuchMethodError();
    }

    @Override
    public void setOutputDefaultSchema(boolean outputDefaultSchema) {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean isDefaultSchema(String catalog, String schema) {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean isDefaultCatalog(String catalog) {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean getOutputDefaultCatalog() {
        throw new NoSuchMethodError();
    }

    @Override
    public void setOutputDefaultCatalog(boolean outputDefaultCatalog) {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean supportsPrimaryKeyNames() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean supportsNotNullConstraintNames() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean supportsBatchUpdates() {
        throw new NoSuchMethodError();
    }

    @Override
    public boolean requiresExplicitNullForColumns() {
        throw new NoSuchMethodError();
    }

    @Override
    public String getSystemSchema() {
        throw new NoSuchMethodError();
    }

    @Override
    public void addReservedWords(Collection<String> words) {
        throw new NoSuchMethodError();
    }

    @Override
    public String escapeDataTypeName(String dataTypeName) {
        throw new NoSuchMethodError();
    }

    @Override
    public String unescapeDataTypeName(String dataTypeName) {
        throw new NoSuchMethodError();
    }

    @Override
    public String unescapeDataTypeString(String dataTypeString) {
        throw new NoSuchMethodError();
    }

    @Override
    public ValidationErrors validate() {
        throw new NoSuchMethodError();
    }
}
