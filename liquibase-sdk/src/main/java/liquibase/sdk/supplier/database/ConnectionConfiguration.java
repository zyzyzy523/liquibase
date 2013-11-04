package liquibase.sdk.supplier.database;

public abstract class ConnectionConfiguration {

    public static final String NAME_STANDARD = "standard";

    public abstract String getDatabaseShortName();
    public abstract String getConfigurationName();

    public abstract String getUrl();

    public String getUsername() {
        return "liquibase";
    }

    public String getPassword() {
        return "liquibase";
    }

    public String getHostName() {
        return "10.10.100.100";
    }


}
