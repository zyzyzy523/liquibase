package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DatabaseDataType;

public class CurrencyTypeCore extends CurrencyType {

    @Override
    public boolean supports(Database database) {
        return super.supports(database);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE - 1;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        if (database instanceof InformixDatabase || database instanceof MSSQLDatabase || database instanceof SybaseASADatabase || database instanceof SybaseDatabase) {
            return new DatabaseDataType("MONEY");
        }
        if (database instanceof OracleDatabase) {
            return new DatabaseDataType("NUMBER", 15, 2);
        }

        if (database instanceof DB2Database) {
            return new DatabaseDataType("DECIMAL", 19,4);
        }
        if (database instanceof FirebirdDatabase) {
            return new DatabaseDataType("DECIMAL", 18, 4);
        }
        if (database instanceof SQLiteDatabase) {
            return new DatabaseDataType("REAL");
        }
        return super.toDatabaseDataType(database);
    }

}
