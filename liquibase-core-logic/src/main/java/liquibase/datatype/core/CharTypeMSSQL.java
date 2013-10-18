package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.MSSQLDatabase;
import liquibase.util.StringUtils;

public class CharTypeMSSQL extends CharType {

    @Override
    public boolean supports(Database database) {
        return database instanceof MSSQLDatabase;
    }

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public String objectToSql(Object value, Database database) {
        String returnValue = super.objectToSql(value, database);
        if (returnValue == null) {
            return null;
        }

        if (!StringUtils.isAscii(returnValue)) {
            return "N"+returnValue;
        }

        return returnValue;
    }
}
