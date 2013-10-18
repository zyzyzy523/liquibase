package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DatabaseDataType;

public class ClobTypeCore extends ClobType  {

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
        if (database instanceof CacheDatabase) {
            return new DatabaseDataType("LONGVARCHAR");
        }   else if (database instanceof FirebirdDatabase) {
            return new DatabaseDataType("BLOB SUB_TYPE TEXT");
        } else if (database instanceof MaxDBDatabase || database instanceof SybaseASADatabase) {
            return new DatabaseDataType("LONG VARCHAR");
        } else if (database instanceof MSSQLDatabase) {
            return new DatabaseDataType("NVARCHAR", "MAX");
        } else if (database instanceof MySQLDatabase) {
            return new DatabaseDataType("LONGTEXT");
        } else if (database instanceof PostgresDatabase || database instanceof SQLiteDatabase || database instanceof SybaseDatabase) {
            return new DatabaseDataType("TEXT");
        } else if (database instanceof OracleDatabase) {
            return new DatabaseDataType("CLOB");
        }
        return super.toDatabaseDataType(database);
    }


}
