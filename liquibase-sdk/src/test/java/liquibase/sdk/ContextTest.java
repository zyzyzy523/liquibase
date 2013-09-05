package liquibase.sdk;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ContextTest {

    private Context context;

    @Before
    public void setup() {
        Context.reset();
        this.context = Context.getInstance();
    }

    @Test
    public void init() throws Exception {
        context.init(new HashSet<String>(Arrays.asList("liquibase.change", "liquibase.changelog", "liquibase.parser")));

        assertThat(context.getPackages(), (Matcher) containsInAnyOrder("liquibase.change", "liquibase.changelog", "liquibase.parser"));
        assertTrue(context.getAllClasses().size() > 0);
        assertThat(context.getAllClasses(), (Matcher) everyItem(hasProperty("name", anyOf(
                startsWith("liquibase.change"),
                startsWith("liquibase.changelog"),
                startsWith("liquibase.parser")
        ))));

        assertTrue(context.getSeenExtensionClasses().size() > 0);
        for (final Class type : context.getSeenExtensionClasses().keySet()) {
            assertThat(context.getSeenExtensionClasses().get(type), everyItem(typeCompatibleWith(type)));
            assertThat(context.getSeenExtensionClasses().get(type), everyItem(not(equalTo(type))));
        }
    }
}
