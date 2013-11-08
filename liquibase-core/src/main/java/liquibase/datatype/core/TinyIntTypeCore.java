package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.DerbyDatabase;
import liquibase.database.core.MSSQLDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.database.core.PostgresDatabase;
import liquibase.datatype.DatabaseDataType;

public class TinyIntTypeCore extends TinyIntType {
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
        if (database instanceof DerbyDatabase || database instanceof PostgresDatabase) {
            return new DatabaseDataType("SMALLINT");
        }
        if (database instanceof MSSQLDatabase) {
            return new DatabaseDataType("TINYINT");
        }
        if (database instanceof OracleDatabase) {
            return new DatabaseDataType("NUMBER",3);
        }
        return super.toDatabaseDataType(database);
    }

}
