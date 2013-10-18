package liquibase.database.core;

public class DatabaseForHash extends H2Database {
    @Override
    public boolean isCaseSensitive() {
        return true;
    }

    @Override
    public String getShortName() {
        return "liquibase-hash";
    }
}
