package liquibase.sdk.change;

import liquibase.change.Change;
import liquibase.sdk.Context;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.fail;

public class StandardChangeTests {

    @Test
    public void allFoundClassesAreRegistered() {
        Context context = Context.getInstance();
        Set<Class> classes = context.getSeenExtensionClasses().get(Change.class);
        if (classes != null) {
            for (Class clazz : classes) {
                try {
                    clazz.newInstance();
                } catch (Throwable e) {
                    fail("Error instantiating " + clazz.getName() + ", extension classes need a public no-arg constructor: " + e.getMessage());
                }
            }
        }
    }
}
