package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DatabaseDataType;

public class DoubleTypeCore extends DoubleType {

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
        if (database instanceof MSSQLDatabase) {
            return new DatabaseDataType("FLOAT");
        }
        if (database instanceof DB2Database || database instanceof DerbyDatabase || database instanceof HsqlDatabase || database instanceof MySQLDatabase) {
            return new DatabaseDataType("DOUBLE");
        }
        if (database instanceof OracleDatabase) {
            return new DatabaseDataType("FLOAT", 24);
        }
        if (database instanceof PostgresDatabase) {
            return new DatabaseDataType("DOUBLE PRECISION");
        }
        if (database instanceof InformixDatabase) {
            return new DatabaseDataType("DOUBLE PRECISION");
        }
        return super.toDatabaseDataType(database);
    }

}
