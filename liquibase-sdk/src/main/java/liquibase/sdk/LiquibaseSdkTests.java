package liquibase.sdk;

import liquibase.sdk.change.StandardChangeTests;
import liquibase.util.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(Suite.class)
@Suite.SuiteClasses({StandardChangeTests.class, LiquibaseSdkTests.TestEnvironmentCheck.class})
public class LiquibaseSdkTests {

    public static class TestEnvironmentCheck {
        @Test
        public void contextInitialized() {
            if (!Context.getInstance().isInitialized()) {
                fail("Testing Context is not initialized. Create a "+Context.getPropertiesFileName()+" file in the root of your classpath.");
            }
        }

        @Test
        public void classesFound() {
            if (Context.getInstance().isInitialized()) {
                assertTrue("No extension classes were found in "+ StringUtils.join(Context.getInstance().getPackages(), ","), Context.getInstance().getSeenExtensionClasses().size() > 0);
            }
        }
    }

}
