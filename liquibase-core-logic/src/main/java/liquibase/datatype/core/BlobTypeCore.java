package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DatabaseDataType;

public class BlobTypeCore extends BlobType {
    @Override
    public boolean supports(Database database) {
        return true;
    }

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE -1;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        if (database instanceof CacheDatabase || database instanceof H2Database || database instanceof HsqlDatabase) {
            return new DatabaseDataType("LONGVARBINARY");
        }
        if (database instanceof MaxDBDatabase) {
            return new DatabaseDataType("LONG BYTE");
        }
        if (database instanceof MSSQLDatabase) {
            String param = "MAX";
            if (this.getParameters().length > 0) {
                param = this.getParameters()[0].toString();
            }
            if (param.equals("2147483647")) {
                param = "MAX";
            }
            return new DatabaseDataType("VARBINARY", param);
        }
        if (database instanceof MySQLDatabase) {
            return new DatabaseDataType("LONGBLOB");
        }
        if (database instanceof PostgresDatabase) {
            return new DatabaseDataType("BYTEA");
        }
        if (database instanceof SybaseASADatabase) {
            return new DatabaseDataType("LONG BINARY");
        }
        if (database instanceof SybaseDatabase) {
            return new DatabaseDataType("IMAGE");
        }
        if (database instanceof OracleDatabase) {
            return new DatabaseDataType("BLOB");
        }

        if (database instanceof FirebirdDatabase) {
            return new DatabaseDataType("BLOB");
        }
        return super.toDatabaseDataType(database);
    }

}
