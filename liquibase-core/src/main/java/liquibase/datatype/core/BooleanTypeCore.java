package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DatabaseDataType;

public class BooleanTypeCore extends BooleanType {

    @Override
    public boolean supports(Database database) {
        return true;
    }

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE - 1;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        if (database instanceof DB2Database || database instanceof FirebirdDatabase) {
            return new DatabaseDataType("SMALLINT");
        } else if (database instanceof MSSQLDatabase) {
            return new DatabaseDataType("BIT");
        } else if (database instanceof MySQLDatabase) {
            return new DatabaseDataType("BIT", 1);
        } else if (database instanceof OracleDatabase) {
            return new DatabaseDataType("NUMBER", 1);
        } else if (database instanceof SybaseASADatabase || database instanceof SybaseDatabase) {
            return new DatabaseDataType("BIT");
        } else if (database instanceof DerbyDatabase) {
            if (((DerbyDatabase) database).supportsBooleanDataType()) {
                return new DatabaseDataType("BOOLEAN");
            } else {
                return new DatabaseDataType("SMALLINT");
            }
        } else if (database instanceof HsqlDatabase) {
            return new DatabaseDataType("BOOLEAN");
        }

        return super.toDatabaseDataType(database);
    }

    @Override
    protected boolean isNumericBoolean(Database database) {
        if (database instanceof DerbyDatabase) {
            return !((DerbyDatabase) database).supportsBooleanDataType();
        }
        return database instanceof DB2Database
                || database instanceof FirebirdDatabase
                || database instanceof MSSQLDatabase
                || database instanceof MySQLDatabase
                || database instanceof OracleDatabase
                || database instanceof SQLiteDatabase
                || database instanceof SybaseASADatabase
                || database instanceof SybaseDatabase;
    }

    /**
     * The database-specific value to use for "false" "boolean" columns.
     */
    @Override
    public String getFalseBooleanValue(Database database) {
        if (isNumericBoolean(database)) {
            return "0";
        }
        if (database instanceof InformixDatabase) {
            return "'f'";
        }
        return "FALSE";
    }

    /**
     * The database-specific value to use for "true" "boolean" columns.
     */
    @Override
    public String getTrueBooleanValue(Database database) {
        if (isNumericBoolean(database)) {
            return "1";
        }
        if (database instanceof InformixDatabase) {
            return "'t'";
        }
        return "TRUE";
    }

}
