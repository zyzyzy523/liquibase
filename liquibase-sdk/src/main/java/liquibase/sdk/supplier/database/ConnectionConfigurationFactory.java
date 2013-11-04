package liquibase.sdk.supplier.database;

import liquibase.database.Database;
import liquibase.servicelocator.ServiceLocator;

import java.util.*;

public class ConnectionConfigurationFactory {

    private static ConnectionConfigurationFactory instance;

    private Map<String, Set<ConnectionConfiguration>> configsByDatabase = new HashMap<String, Set<ConnectionConfiguration>>();

    public ConnectionConfigurationFactory() {
        try {
            Class[] classes = ServiceLocator.getInstance().findClasses(ConnectionConfiguration.class);

            //noinspection unchecked
            for (Class<? extends ConnectionConfiguration> clazz : classes) {
                register(clazz.getConstructor().newInstance());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static ConnectionConfigurationFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionConfigurationFactory();
        }
        return instance;
    }

    public Set<ConnectionConfiguration> getConfigurations(Database database) {
        Set<ConnectionConfiguration> configurations = configsByDatabase.get(database.getShortName());
        if (configurations == null) {
            return new HashSet<ConnectionConfiguration>();
        }
        return configurations;
    }

    public static void reset() {
        instance = new ConnectionConfigurationFactory();
    }

    public void register(ConnectionConfiguration config) {
        String databaseShortName = config.getDatabaseShortName();
        if (!configsByDatabase.containsKey(databaseShortName)) {
            configsByDatabase.put(databaseShortName, new HashSet<ConnectionConfiguration>());
        }
        configsByDatabase.get(databaseShortName).add(config);
    }

}
