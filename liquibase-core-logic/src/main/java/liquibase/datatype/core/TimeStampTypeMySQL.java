package liquibase.datatype.core;

import liquibase.database.Database;
import liquibase.database.core.MySQLDatabase;
import liquibase.datatype.DatabaseDataType;

public class TimeStampTypeMySQL extends TimestampType {
    @Override
    public boolean supports(Database database) {
        return database instanceof MySQLDatabase;
    }

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        return new DatabaseDataType("TIMESTAMP");
    }
}
