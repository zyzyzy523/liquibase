package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.HsqlDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.datatype.DatabaseDataType;

public class NCharTypeCore extends NCharType {
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
        if (database instanceof HsqlDatabase) {
            return new DatabaseDataType("CHAR", getParameters());
        }
        if (database instanceof OracleDatabase) {
            return new DatabaseDataType("NCHAR2", getParameters());
        }
        return super.toDatabaseDataType(database);
    }

}
