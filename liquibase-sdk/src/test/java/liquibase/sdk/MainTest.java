package liquibase.sdk;

import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hamcrest.core.IsEqual;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.hamcrest.object.IsCompatibleType;
import org.junit.Test;

import java.util.Arrays;


public class MainTest {

    @Test
    public void init_noArguments() throws Exception {
        Main main = new Main();
        try {
            main.init(new String[0]);
            fail("Did not throw exception");
        } catch (Main.UserError e) {
            assertEquals("Error parsing command line: Missing required option: packages", e.getMessage());
        }
    }

    @Test
    public void init() throws Exception {
        Main main = new Main();
        main.init(new String[] {
                "-packages",
                "liquibase.change"
        });

        assertThat(main.getPackages(), (Matcher) containsInAnyOrder("liquibase.change"));
        assertTrue(main.getAllClasses().size() > 0);
        assertThat(main.getAllClasses(),(Matcher) everyItem(hasProperty("name", startsWith("liquibase.change"))));
    }

    @Test
    public void init_multiplePackages() throws Exception {
        Main main = new Main();
        main.init(new String[] {
                "-packages",
                "liquibase.change, liquibase.changelog, liquibase.parser"
        });

        assertThat(main.getPackages(), (Matcher) containsInAnyOrder("liquibase.change", "liquibase.changelog", "liquibase.parser"));
        assertTrue(main.getAllClasses().size() > 0);
        assertThat(main.getAllClasses(), (Matcher) everyItem(hasProperty("name", anyOf(
                startsWith("liquibase.change"),
                startsWith("liquibase.changelog"),
                startsWith("liquibase.parser")
        ))));

        assertTrue(main.getSeenExtensionClasses().size() > 0);
        for (final Class type : main.getSeenExtensionClasses().keySet()) {
            assertThat(main.getSeenExtensionClasses().get(type), everyItem(typeCompatibleWith(type)));
        }
    }
}
