package liquibase.database.core.config;

import liquibase.sdk.supplier.database.ConnectionConfiguration;

public class OracleConfigStandard extends ConnectionConfiguration {
    @Override
    public String getDatabaseShortName() {
        return "oracle";
    }

    @Override
    public String getConfigurationName() {
        return NAME_STANDARD;
    }

    @Override
    public String getUrl() {
        return "jdbc:oracle:thin:@" + getHostName() + ":1521:XE";
    }
}
