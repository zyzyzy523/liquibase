package liquibase.database.core.config;

import liquibase.sdk.supplier.database.ConnectionConfiguration;

public class MSSQLConfigStandard extends ConnectionConfiguration {
    @Override
    public String getDatabaseShortName() {
        return "mssql";
    }

    @Override
    public String getConfigurationName() {
        return NAME_STANDARD;
    }

    @Override
    public String getUrl() {
        return "jdbc:sqlserver://"+ getHostName() +":1433;databaseName=liquibase";
    }
}
