package liquibase.sdk.supplier.database;

import java.util.HashSet;
import java.util.Set;

public abstract class ConnectionConfiguration implements Cloneable {

    public static final String NAME_STANDARD = "standard";

    public String version;
    private String hostname = "10.10.100.100";

    public abstract String getDatabaseShortName();
    public abstract String getConfigurationName();

    public abstract String getUrl();

    public String getUsername() {
        return "liquibase";
    }

    public String getPassword() {
        return "liquibase";
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
