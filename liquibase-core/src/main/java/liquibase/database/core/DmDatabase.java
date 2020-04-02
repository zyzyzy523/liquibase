package liquibase.database.core;


import liquibase.CatalogAndSchema;
import liquibase.database.DatabaseConnection;


import liquibase.exception.DatabaseException;
import liquibase.database.AbstractJdbcDatabase;
import liquibase.executor.ExecutorService;
import liquibase.logging.LogService;
import liquibase.logging.LogType;
import liquibase.statement.DatabaseFunction;
import liquibase.statement.SequenceCurrentValueFunction;
import liquibase.statement.SequenceNextValueFunction;
import liquibase.statement.core.RawCallStatement;
import liquibase.structure.core.Schema;

import java.util.Locale;


public class DmDatabase extends AbstractJdbcDatabase {

    public static final String PRODUCT_NAME = "DM DBMS";

    /**
     * Default constructor for an object that represents the Oracle Database DBMS.
     */
    public DmDatabase() {
        super.unquotedObjectsAreUppercased = true;
        //noinspection HardCodedStringLiteral
        super.setCurrentDateTimeFunction("SYSTIMESTAMP");
        // Setting list of Oracle's native functions
        //noinspection HardCodedStringLiteral
        dateFunctions.add(new DatabaseFunction("SYSDATE"));
        //noinspection HardCodedStringLiteral
        dateFunctions.add(new DatabaseFunction("SYSTIMESTAMP"));
        //noinspection HardCodedStringLiteral
        dateFunctions.add(new DatabaseFunction("CURRENT_TIMESTAMP"));
        //noinspection HardCodedStringLiteral
        super.sequenceNextValueFunction = "%s.nextval";
        //noinspection HardCodedStringLiteral
        super.sequenceCurrentValueFunction = "%s.currval";
    }

    @Override
    public void setConnection(DatabaseConnection conn) {
        super.setConnection(conn);
    }


    @Override
    protected String getDefaultDatabaseProductName() {
        //noinspection HardCodedStringLiteral
        return "dm";
    }

    @Override
    public Integer getDefaultPort() {
        return 1521;
    }

    @Override
    public String getJdbcCatalogName(CatalogAndSchema schema) {
        return null;
    }

    @Override
    public String getJdbcSchemaName(CatalogAndSchema schema) {
        return correctObjectName((schema.getCatalogName() == null) ? schema.getSchemaName() : schema.getCatalogName(), Schema.class);
    }

    @Override
    protected String getAutoIncrementClause() {
        return "AUTO_INCREMENT";
    }

    @Override
    public String generatePrimaryKeyName(String tableName) {
        if (tableName.length() > 27) {
            //noinspection HardCodedStringLiteral
            return "PK_" + tableName.toUpperCase(Locale.US).substring(0, 27);
        } else {
            //noinspection HardCodedStringLiteral
            return "PK_" + tableName.toUpperCase(Locale.US);
        }
    }

    @Override
    public boolean supportsInitiallyDeferrableColumns() {
        return true;
    }

    @Override
    public boolean supportsSequences() {
        return false;
    }

    /**
     * Oracle supports catalogs in liquibase terms
     *
     * @return false
     */
    @Override
    public boolean supportsSchemas() {
        return true;
    }



    @Override
    public String getDefaultCatalogName() {//NOPMD
        return (super.getDefaultCatalogName() == null) ? null : super.getDefaultCatalogName().toUpperCase(Locale.US);
    }

    @Override
    public boolean supportsTablespaces() {
        return false;
    }

    @Override
    public boolean supportsAutoIncrement() {
        return true;
    }


    @Override
    public boolean supportsRestrictForeignKeys() {
        return false;
    }



    @Override
    public boolean jdbcCallsCatalogsSchemas() {
        return true;
    }



    @Override
    public String generateDatabaseFunctionValue(DatabaseFunction databaseFunction) {
        //noinspection HardCodedStringLiteral
        if ((databaseFunction != null) && "current_timestamp".equalsIgnoreCase(databaseFunction.toString())) {
            return databaseFunction.toString();
        }
        if ((databaseFunction instanceof SequenceNextValueFunction) || (databaseFunction instanceof
                SequenceCurrentValueFunction)) {
            String quotedSeq = super.generateDatabaseFunctionValue(databaseFunction);
            // replace "myschema.my_seq".nextval with "myschema"."my_seq".nextval
            return quotedSeq.replaceFirst("\"([^\\.\"]+)\\.([^\\.\"]+)\"", "\"$1\".\"$2\"");

        }

        return super.generateDatabaseFunctionValue(databaseFunction);
    }



    @Override
    public boolean supportsNotNullConstraintNames() {
        return true;
    }




    @Override
    public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
        return PRODUCT_NAME.equalsIgnoreCase(conn.getDatabaseProductName());
    }

    @Override
    protected String getConnectionSchemaName() {
        return getConnectionCatalogName();
    }

    @Override
    public String getDefaultDriver(String url) {
        //noinspection HardCodedStringLiteral
        if (url.startsWith("jdbc:dm")) {
            return "dm.jdbc.driver.DmDriver";
        }
        return null;
    }

    @Override
    public String getShortName() {
        return "dm";
    }

    @Override
    protected String getConnectionCatalogName() {
        try {
            //noinspection HardCodedStringLiteral
            return ExecutorService.getInstance().getExecutor(this).queryForObject(new RawCallStatement("select sys_context( 'userenv', 'current_schema' ) from dual"), String.class);
        } catch (Exception e) {
            //noinspection HardCodedStringLiteral
            LogService.getLog(getClass()).info(LogType.LOG, "Error getting default schema", e);
        }
        return null;
    }


    @Override
    public int getPriority() {
        return PRIORITY_DEFAULT;
    }


}
