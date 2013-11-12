package liquibase.database.core.config;

import liquibase.sdk.supplier.database.ConnectionConfiguration;

import java.util.Arrays;
import java.util.Set;

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
        return "jdbc:oracle:thin:@" + getHostname() + ":1521:XE";
    }

    @Override
    public Set<String> getPuppetModules() {
        Set<String> modules = super.getPuppetModules();
        modules.add("biemond/oradb");
        return modules;
    }

    @Override
    public String getVagrantBoxName() {
        return "linux.centos.6_4";
    }

    @Override
    public Set<String> getRequiredPackages(String vagrantBoxName) {
        Set<String> requiredPackages = super.getRequiredPackages(vagrantBoxName);
        requiredPackages.addAll(Arrays.asList("binutils",
                "compat-libcap1",
                "gcc",
                "gcc-c++",
                "glibc",
                "glibc-devel",
                "ksh",
                "libgcc",
                "libstdc++",
                "libstdc++-devel",
                "libaio",
                "libaio-devel",
                "libXext",
                "libX11",
                "libXau",
                "libxcb",
                "libXi",
                "make",
                "sysstat"
                ));

        return requiredPackages;
    }

    @Override
    public String getPuppetInit(String box) {
        return "package { $oracle_packages: ensure => \"installed\" }\n" +
                "\n" +
                "oradb::installdb{ '12.1.0.1_Linux-x86-64':\n" +
                "        version      => '12.1.0.1',\n" +
                "        file         => 'linuxamd64_12c_database',\n" +
                "        databaseType => 'SE',\n" +
                "        oracleBase   => '/oracle',\n" +
                "        oracleHome   => '/oracle/product/12.1/db',\n" +
                "        user         => 'oracle',\n" +
                "        group        => 'dba',\n" +
                "        downloadDir  => '/install/oracle/',\n" +
                "        puppetDownloadMntPoint  => '/install/oracle/'\n" +
                "}\n" +
                "\n" +
                "oradb::database{ 'liquibase':\n" +
                "                  oracleBase              => '/oracle',\n" +
                "                  oracleHome              => '/oracle/product/12.1/db',\n" +
                "                  version                 => \"12.1\",\n" +
                "                  user                    => 'oracle',\n" +
                "                  group                   => 'dba',\n" +
                "                  downloadDir             => '/install/oracle/',\n" +
                "                  action                  => 'create',\n" +
                "                  dbName                  => 'liquibase',\n" +
                "                  dbDomain                => 'liquibase.org',\n" +
                "                  sysPassword             => 'oracle',\n" +
                "                  systemPassword          => 'oracle',\n" +
                "                  dataFileDestination     => \"/oracle/oradata\",\n" +
                "                  recoveryAreaDestination => \"/oracle/flash_recovery_area\",\n" +
                "                  characterSet            => \"AL32UTF8\",\n" +
                "                  nationalCharacterSet    => \"UTF8\",\n" +
                "                  initParams              => \"open_cursors=1000,processes=600,job_queue_processes=4\",\n" +
                "                  sampleSchema            => 'FALSE',\n" +
                "                  memoryPercentage        => \"40\",\n" +
                "                  memoryTotal             => \"800\",\n" +
                "                  databaseType            => \"MULTIPURPOSE\",\n" +
                "                  require                 => Oradb::InstallDb['12.1.0.1_Linux-x86-64'],\n" +
                "}\n" +
                "\n" +
                " oradb::net{ 'config net8':\n" +
                "        oracleHome   => '/oracle/product/12.1/db',\n" +
                "        version      => \"12.1\",\n" +
                "        user         => 'oracle',\n" +
                "        group        => 'dba',\n" +
                "        downloadDir  => '/install/oracle/',\n" +
                "        require      => Oradb::Database['liquibase'],\n" +
                "   }\n" +
                "\n" +
                "oradb::listener{'start listener':\n" +
                "        oracleBase   => '/oracle',\n" +
                "        oracleHome   => '/oracle/product/12.1/db',\n" +
                "        user         => 'oracle',\n" +
                "        group        => 'dba',\n" +
                "        action       => 'start',\n" +
                "        require      => Oradb::Net['config net8'],\n" +
                "   }\n" +
                "\n" +
                "oradb::autostartdatabase{ 'autostart oracle':\n" +
                "                   oracleHome              => '/oracle/product/12.1/db',\n" +
                "                   user                    => 'oracle',\n" +
                "                   dbName                  => 'liquibase',\n" +
                "                   require                 => Oradb::Database['liquibase'],\n" +
                "}";
    }
}
