package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.DB2Database;
import liquibase.database.core.DerbyDatabase;

public class DateTypeCore extends DateType {
    @Override
    public boolean supports(Database database) {
        return true;
    }

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE - 1;
    }

    @Override
    public Object sqlToObject(String value, Database database) {
        if (database instanceof DB2Database) {
            return value.replaceFirst("^\"SYSIBM\".\"DATE\"\\('", "").replaceFirst("'\\)", "");
        }
        if (database instanceof DerbyDatabase) {
            return value.replaceFirst("^DATE\\('", "").replaceFirst("'\\)", "");
        }

        return super.sqlToObject(value, database);
    }
}
