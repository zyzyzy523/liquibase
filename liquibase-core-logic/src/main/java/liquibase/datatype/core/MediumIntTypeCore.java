package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.DB2Database;
import liquibase.database.core.DerbyDatabase;
import liquibase.database.core.FirebirdDatabase;
import liquibase.database.core.MSSQLDatabase;
import liquibase.datatype.DatabaseDataType;

public class MediumIntTypeCore extends MediumIntType {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE - 1;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        if (database instanceof DB2Database || database instanceof DerbyDatabase || database instanceof FirebirdDatabase || database instanceof MSSQLDatabase) {
            return new DatabaseDataType("MEDIUMINT"); //always smallint regardless of parameters passed
        }
        return super.toDatabaseDataType(database);
    }

}
