package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.FirebirdDatabase;
import liquibase.datatype.DatabaseDataType;

public class FloatTypeCore extends FloatType {

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
        if (database instanceof FirebirdDatabase) {
            return new DatabaseDataType("FLOAT");
        }
        return super.toDatabaseDataType(database);
    }

}
