package liquibase.sdk.verify;

import liquibase.sdk.state.OutputFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class TestStateTest {

//    private TestInformation testInformation;
//
//    @Before
//    public void setup() {
//        testInformation = new TestInformation();
//        testInformation.starting(Description.createSuiteDescription(TestStateTest.class));
//    }
//
//    @Test
//    public void getSavedStateDir() throws Exception {
//        assertThat(new TestState(testInformation, TestState.Type.SQL).getSavedStateDir().getAbsolutePath().replace("\\", "/"), endsWith("liquibase-sdk/src/test/java/liquibase/sdk/verify/approved_output"));
//    }
//
//    @Test
//    public void serializeData() {
//        PermutationOutput output = new PermutationOutput();
//
//        output.set("key 1", "value 1");
//        assertEquals("key 1 => value 1", output.toString());
//
//        output.set("key null", null);
//        assertEquals("key 1 => value 1\nkey null => NULL", output.toString());
//
//        output.set("earlier alphabetically", "value 2");
//        assertEquals("earlier alphabetically => value 2\nkey 1 => value 1\nkey null => NULL", output.toString());
//
//        Collection listData = Arrays.asList("a", "b", "c");
//        output.set("list data", listData, new OutputFormat.CollectionFormat());
//        assertEquals("earlier alphabetically => value 2\n"+
//                "key 1 => value 1\n"+
//                "key null => NULL\n"+
//                "list data => a, b, c", output.toString());
//
//
//    }
//
//    @Test
//    public void outputNextPermutation() {
//        Permutation previousPermutation = null;
//        Permutation thisPermutation = new Permutation();
//        thisPermutation.pushEntry("level 1", "a");
//        assertEquals("# level 1: a\n" +
//                "\n" +
//                "*****\n" +
//                "\n" +
//                "**NO PERMUTATION DATA**", new TestState(testInformation, TestState.Type.SQL).outputNextPermutation(thisPermutation, previousPermutation, new PermutationOutput()));
//
//
//        previousPermutation = thisPermutation;
//        thisPermutation = new Permutation();
//        thisPermutation.pushEntry("level 1", "b");
//        assertEquals("# level 1: b\n" +
//                "\n" +
//                "*****\n" +
//                "\n" +
//                "**NO PERMUTATION DATA**", new TestState(testInformation, TestState.Type.SQL).outputNextPermutation(thisPermutation, previousPermutation, new PermutationOutput()));
//
//
//        previousPermutation = new Permutation();
//        previousPermutation.pushEntry("level 1", "b");
//        previousPermutation.pushEntry("level 2", "X");
//        thisPermutation = new Permutation();
//        thisPermutation.pushEntry("level 1", "b");
//        thisPermutation.pushEntry("level 2", "X");
//        assertEquals("## level 2: X\n" +
//                "\n" +
//                "*****\n" +
//                "\n" +
//                "**NO PERMUTATION DATA**", new TestState(testInformation, TestState.Type.SQL).outputNextPermutation(thisPermutation, previousPermutation, new PermutationOutput()));
//
//        previousPermutation = new Permutation();
//        previousPermutation.pushEntry("level 1", "b");
//        previousPermutation.pushEntry("level 2", "X");
//        thisPermutation = new Permutation();
//        thisPermutation.pushEntry("level x", "b");
//        thisPermutation.pushEntry("level y", "X");
//        assertEquals("# level x: b\n"+
//                "## level y: X\n" +
//                "\n" +
//                "*****\n" +
//                "\n" +
//                "**NO PERMUTATION DATA**", new TestState(testInformation, TestState.Type.SQL).outputNextPermutation(thisPermutation, previousPermutation, new PermutationOutput()));
//    }
}
