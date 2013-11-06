package liquibase.sdk.supplier.database;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    public Set<String> getPuppetModules() {
        return new HashSet<String>();
    }

    public Set<String> getPuppetForges() {
        HashSet<String> forges = new HashSet<String>();
        forges.add("http://forge.puppetlabs.com");

        return forges;
    }

    public String getVagrantBoxName() {
        return "linux";
    }

    public Set<String> getRequiredPackages() {
        return new HashSet<String>();
    }

    public String getPuppetInit() {
        return null;
    }
}
