package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DatabaseDataType;

public class NVarcharTypeCore extends NVarcharType {
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
        if (database instanceof HsqlDatabase || database instanceof PostgresDatabase || database instanceof DerbyDatabase) {
            return new DatabaseDataType("VARCHAR", getParameters());
        }
        if (database instanceof OracleDatabase) {
            return new DatabaseDataType("NVARCHAR2", getParameters());
        }
        if (database instanceof MSSQLDatabase) {
            if (getParameters() != null && getParameters().length > 0) {
                Object param1 = getParameters()[0];
                if (param1.toString().matches("\\d+")) {
                    if (Long.valueOf(param1.toString()) > 8000) {
                        return new DatabaseDataType("NVARCHAR", "MAX");
                    }
                }
            }
            return new DatabaseDataType("NVARCHAR", getParameters());
        }
        return super.toDatabaseDataType(database);
    }


}
