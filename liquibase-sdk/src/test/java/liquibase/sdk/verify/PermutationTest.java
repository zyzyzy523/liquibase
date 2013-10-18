package liquibase.sdk.verify;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class PermutationTest {

//    @Test
//    public void serialize() {
//        Permutation permutation = new Permutation();
//        PermutationOutput output = new PermutationOutput();
//
//        permutation.pushEntry("Change Class", CreateTableChange.class);
//        assertEquals("# Change Class: liquibase.change.core.CreateTableChange\n"+
//                "\n"+
//                "_Permutation Data:_\n"+
//                "    *NONE*", permutation.serialize(output));
//
//
//        permutation.pushEntry("Database", new OracleDatabase());
//        assertEquals("## Database: oracle\n"+
//                "\n"+
//                "_Permutation Data:_\n"+
//                "    *NONE*", permutation.serialize(output));
//
//        output.set("key 1", "value 1");
//        assertEquals("## Database: oracle\n"+
//                "\n"+
//                "_Permutation Data:_\n"+
//                "    *key 1:* value 1", permutation.serialize(output));
//
//        output.set("key 2", "value 2");
//        assertEquals("## Database: oracle\n"+
//                "\n"+
//                "_Permutation Data:_\n"+
//                "    *key 1:* value 1\n"+
//                "    \n"+
//                "    *****\n"+
//                "    \n"+
//                "    *key 2:* value 2", permutation.serialize(output));
//    }
//
//    @Test
//    public void getParentStack() {
//        Permutation permutation = new Permutation();
//
//        assertEquals(0, permutation.getParentStack().length);
//
//        permutation.pushEntry("first", "1");
//        assertEquals(0, permutation.getParentStack().length);
//
//        permutation.pushEntry("second", "2");
//        assertThat(Arrays.asList(permutation.getParentStack()),contains("# first: 1"));
//
//        permutation.pushEntry("third", "3");
//        assertThat(Arrays.asList(permutation.getParentStack()),contains("# first: 1", "## second: 2"));
//    }
}
