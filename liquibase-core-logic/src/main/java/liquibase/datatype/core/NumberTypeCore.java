package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DatabaseDataType;

public class NumberTypeCore extends NumberType {

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
        if (database instanceof MySQLDatabase
                || database instanceof DB2Database
                || database instanceof MSSQLDatabase
                || database instanceof HsqlDatabase
                || database instanceof DerbyDatabase
                || database instanceof PostgresDatabase
                || database instanceof FirebirdDatabase
                || database instanceof SybaseASADatabase
                || database instanceof SybaseDatabase) {
            return new DatabaseDataType("numeric", getParameters());
        }

        if (database instanceof OracleDatabase) {
            if (getParameters().length > 0 && getParameters()[0].equals("0") && getParameters()[1].equals("-127")) {
                return new DatabaseDataType("NUMBER");
            } else {
                return new DatabaseDataType("NUMBER", getParameters());
            }
        }
        return super.toDatabaseDataType(database);
    }


}
