package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DatabaseDataType;
import liquibase.exception.DatabaseException;

public class TimeTypeCore extends TimeType {

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
        if (database instanceof InformixDatabase) {
            return new DatabaseDataType("INTERVAL HOUR TO FRACTION", 5);
        }
        if (database instanceof MSSQLDatabase) {
            try {
                if (database.getDatabaseMajorVersion() <= 9) {
                    return new DatabaseDataType("DATETIME");
                }
            } catch (DatabaseException e) {
                //assume greater than sql 2008 and TIME will work
            }
        }
        if (database instanceof OracleDatabase) {
            return new DatabaseDataType("DATE");
        }
        return super.toDatabaseDataType(database);
    }

    @Override
    public Object sqlToObject(String value, Database database) {
        if (database instanceof DB2Database) {
            return value.replaceFirst("^\"SYSIBM\".\"TIME\"\\('", "").replaceFirst("'\\)", "");
        }
        if (database instanceof DerbyDatabase) {
            return value.replaceFirst("^TIME\\('", "").replaceFirst("'\\)", "");
        }

        return super.sqlToObject(value, database);
    }

}
