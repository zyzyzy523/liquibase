package liquibase.database.core.config;

import liquibase.sdk.supplier.database.ConnectionConfiguration;

public class CacheConfigStandard extends ConnectionConfiguration {
    @Override
    public String getDatabaseShortName() {
        return "cache";
    }

    @Override
    public String getConfigurationName() {
        return NAME_STANDARD;
    }

    @Override
    public String getUrl() {
        return "jdbc:Cache://"+ getHostname() +":1972/liquibase";
    }
}
