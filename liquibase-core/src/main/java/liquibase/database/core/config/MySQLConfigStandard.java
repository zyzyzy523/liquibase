package liquibase.database.core.config;

import liquibase.sdk.supplier.database.ConnectionConfiguration;

import java.util.Set;

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
        return "jdbc:mysql://"+ getHostname() +"/liquibase";
    }

    @Override
    public Set<String> getPuppetModules() {
        Set<String> modules = super.getPuppetModules();
        modules.add("puppetlabs/mysql");
        return modules;
    }

    @Override
    public Set<String> getRequiredPackages() {
        return super.getRequiredPackages();
    }

    @Override
    public String getPuppetInit() {
        return "class { '::mysql::server':\n" +
                "    root_password => 'root',\n"+
                (getVersion() == null ? "" : "    package_ensure => '"+getVersion()+"',\n")+
                "    override_options => { 'mysqld' => { 'bind_address'  => '0.0.0.0' } }, \n" +
                "}\n" +
                "\n" +
                "mysql::db { 'liquibase':\n" +
                "  user     => '"+getUsername()+"',\n" +
                "  password => '"+getPassword()+"',\n" +
                "  host     => '%',\n" +
                "  grant    => ['all'],\n" +
                "}\n" +
                "\n" +
                "mysql::db { 'liquibaseb':\n" +
                "  user     => '"+getUsername()+"',\n" +
                "  password => '"+getPassword()+"',\n" +
                "  host     => '%',\n" +
                "  grant    => ['all'],\n" +
                "}\n";
    }
}
