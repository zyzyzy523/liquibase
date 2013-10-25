package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DatabaseDataType;

public class DateTimeTypeCore extends DateTimeType {

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
        if (database instanceof DB2Database
                || database instanceof DerbyDatabase
                || database instanceof FirebirdDatabase
                || database instanceof H2Database
                || database instanceof HsqlDatabase
                || database instanceof MaxDBDatabase
                || database instanceof OracleDatabase) {
            return new DatabaseDataType("TIMESTAMP");
        }

        if (database instanceof MSSQLDatabase && getParameters().length > 0 && "16".equals(getParameters()[0])) {
            return new DatabaseDataType("SMALLDATETIME");
        }
        if (database instanceof InformixDatabase) {
            return new DatabaseDataType("DATETIME YEAR TO FRACTION", 5);
        }
        if (database instanceof PostgresDatabase) {
            return new DatabaseDataType("TIMESTAMP WITH TIME ZONE");
        }
        if (database instanceof SQLiteDatabase) {
            return new DatabaseDataType("TEXT");
        }

        if (database instanceof OracleDatabase) {
            return new DatabaseDataType("DATE");
        }

        return new DatabaseDataType(getName());
    }

    @Override
    public Object sqlToObject(String value, Database database) {
        if (database instanceof DB2Database) {
            return value.replaceFirst("^\"SYSIBM\".\"TIMESTAMP\"\\('", "").replaceFirst("'\\)", "");
        }
        if (database instanceof DerbyDatabase) {
            return value.replaceFirst("^TIMESTAMP\\('", "").replaceFirst("'\\)", "");
        }
        return super.sqlToObject(value, database);
    }


}
