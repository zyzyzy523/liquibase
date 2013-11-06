package liquibase.database.core.config;

import liquibase.sdk.supplier.database.ConnectionConfiguration;

public class DB2ConfigStandard extends ConnectionConfiguration {
    @Override
    public String getDatabaseShortName() {
        return "db2";
    }

    @Override
    public String getConfigurationName() {
        return NAME_STANDARD;
    }

    @Override
    public String getUrl() {
        return "jdbc:db2://"+ getHostname() +":50000/lqbase";
    }
}
