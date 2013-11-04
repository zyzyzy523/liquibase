package liquibase.sdk.standardtests.database;

import liquibase.database.Database;
import liquibase.sdk.supplier.database.AllDatabaseTypes;
import liquibase.sdk.supplier.database.ConnectionConfiguration;
import liquibase.sdk.supplier.database.ConnectionConfigurationFactory;
import org.junit.experimental.theories.ParametersSuppliedBy;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

@RunWith(Theories.class)
public class StandardDatabaseTests {

    @Theory
    public void connectionConfigurationExists(@ParametersSuppliedBy(AllDatabaseTypes.class) Database database) {
        assumeFalse(database.getShortName().equals("unsupported"));

        Set<ConnectionConfiguration> configurations = ConnectionConfigurationFactory.getInstance().getConfigurations(database);
        assertTrue("No connection configurations found", configurations.size() > 0);
    }
}
