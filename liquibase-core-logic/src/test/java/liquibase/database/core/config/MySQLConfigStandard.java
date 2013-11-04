package liquibase.database.core.config;

import liquibase.sdk.supplier.database.ConnectionConfiguration;

public class MySQLConfigStandard extends ConnectionConfiguration {
    @Override
    public String getDatabaseShortName() {
        return "mysql";
    }

    @Override
    public String getConfigurationName() {
        return NAME_STANDARD;
    }

    @Override
    public String getUrl() {
        return "jdbc:mysql://"+ getHostName() +"/liquibase";
    }
}
