package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DatabaseDataType;

public class IntTypeCore extends IntType {
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
        if (database instanceof InformixDatabase && isAutoIncrement()) {
            return new DatabaseDataType("SERIAL");
        }
        if (database instanceof OracleDatabase) {
            return new DatabaseDataType("INTEGER");
        }
        if (database instanceof DB2Database || database instanceof DerbyDatabase) {
            return new DatabaseDataType("INTEGER");
        }
        if (database instanceof PostgresDatabase) {
            if (isAutoIncrement()) {
                return new DatabaseDataType("SERIAL");
            }
        }
        if (database instanceof MSSQLDatabase || database instanceof HsqlDatabase || database instanceof FirebirdDatabase) {
            return new DatabaseDataType("INT");
        }
        return super.toDatabaseDataType(database);

        //sqllite
        //        if (columnTypeString.equals("INTEGER") ||
//                columnTypeString.toLowerCase(Locale.ENGLISH).contains("int") ||
//                columnTypeString.toLowerCase(Locale.ENGLISH).contains("bit")) {
//            type = new IntType("INTEGER");
    }

}
