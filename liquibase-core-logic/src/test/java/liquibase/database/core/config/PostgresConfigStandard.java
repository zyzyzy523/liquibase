package liquibase.database.core.config;

import liquibase.sdk.supplier.database.ConnectionConfiguration;

public class PostgresConfigStandard extends ConnectionConfiguration {
    @Override
    public String getDatabaseShortName() {
        return "postgresql";
    }

    @Override
    public String getConfigurationName() {
        return NAME_STANDARD;
    }

    @Override
    public String getUrl() {
        return "jdbc:postgresql://"+ getHostName() +"/liquibase";
    }
}
