package liquibase.database.core;

import liquibase.database.InternalDatabase;

public class DatabaseForHash extends H2Database implements InternalDatabase {
    @Override
    public boolean isCaseSensitive() {
        return true;
    }

    @Override
    public String getShortName() {
        return "liquibase-hash";
    }
}
