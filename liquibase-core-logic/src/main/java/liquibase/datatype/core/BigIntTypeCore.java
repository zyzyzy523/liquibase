package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DatabaseDataType;

public class BigIntTypeCore extends BigIntType {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE - 1;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        if (database instanceof InformixDatabase) {
            if (isAutoIncrement()) {
                return new DatabaseDataType("SERIAL8");
            } else {
                return new DatabaseDataType("INT8");
            }
        }
        if (database instanceof OracleDatabase) {
            return new DatabaseDataType("NUMBER", 38,0);
        }
        if (database instanceof DB2Database || database instanceof DerbyDatabase
                || database instanceof MSSQLDatabase || database instanceof HsqlDatabase || database instanceof FirebirdDatabase) {
            return new DatabaseDataType("BIGINT");
        }
        if (database instanceof PostgresDatabase) {
            if (isAutoIncrement()) {
                return new DatabaseDataType("BIGSERIAL");
            }
        }
        return super.toDatabaseDataType(database);
    }
}
