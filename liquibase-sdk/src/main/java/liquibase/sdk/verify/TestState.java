package liquibase.sdk.verify;

import liquibase.util.StringUtils;
import org.junit.ComparisonFailure;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TestState {
//    private StringBuilder runOutput;
//
//    private Permutation lastPermutation;
//
//    private TestInformation testInformation;
//    private File acceptedOutputFile;
//    private String testName;
//    private String testGroup;
//    private String stateName;
//    private Type type;
//
//    private StringBuilder stateContent = new StringBuilder();
//
//    public TestState(TestInformation testInformation, Type type) {
//        this.testInformation = testInformation;
//        this.type = type;
//
//        this.runOutput = new StringBuilder();
//
//    }
//
//    private File getAcceptedOutputFile() {
//        if (acceptedOutputFile == null) {
//            acceptedOutputFile = new File(getSavedStateDir(), testInformation.getTestClass().getSimpleName()+"."+testInformation.getTestName()+".accepted.md");
//        }
//        return acceptedOutputFile;
//    }
//
//    /**
//     * Return the directory approved_output directory to use for this TestState. Creates approved_output dir if it does not exist
//     */
//    public File getSavedStateDir() {
//        String testPackageDir = testInformation.getTestClass().getPackage().getName().replace(".", "/");
//
//        File sdkPropertiesFile = null;
//        try {
//            URL resource = getClass().getClassLoader().getResource("liquibase.sdk.properties");
//            if (resource == null) {
//                throw new RuntimeException("Could not find liquibase.sdk.properties");
//            }
//            sdkPropertiesFile = new File(resource.toURI());
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//
//        String returnDir = sdkPropertiesFile.getAbsolutePath().replace("\\", "/") //normalize separators
//                .replaceFirst("/liquibase.sdk.properties$", "") //strip off file name
//                .replace("target/test-classes", "src/test/resources"); //switch to src dir
//        File file = new File(returnDir+"/"+testPackageDir);
//        file.mkdirs();
//        if (!file.exists()) {
//            throw new RuntimeException("Expected source path "+file.getAbsolutePath()+" does not exist");
//        }
//
//        file = new File(file, "approved_output");
//        file.mkdir();
//        return file;
//    }
//
//    public void addValue(String value) {
//        stateContent.append(value).append("\n");
//    }
//
//    public void save() throws Exception{
////        savedStateDir.mkdirs();
//
//        BufferedWriter outputStream = new BufferedWriter(new FileWriter(getAcceptedOutputFile()));
//        outputStream.write(stateContent.toString());
//        outputStream.flush();
//        outputStream.close();
//    }
//
//    public void test() throws Exception {
//        String existingContent = readExistingValue();
//        if (existingContent.equals("") && StringUtils.trimToNull(stateContent.toString()) != null) {
//            save();
//        } else {
//            try {
//                assertEquals("Unexpected difference in "+ getAcceptedOutputFile().getAbsolutePath(), existingContent, stateContent.toString());
//            } catch (ComparisonFailure e) {
//                if ("overwrite".equals(System.getProperty("liquibase.verify.mode"))) {
//                    save();
//                } else {
//                    throw e;
//                }
//            }
//        }
//    }
//
//    private String readExistingValue() throws IOException {
//        StringBuilder content = new StringBuilder();
//        if (getAcceptedOutputFile().exists()) {
//            BufferedReader reader = new BufferedReader(new FileReader(getAcceptedOutputFile()));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                content.append(line).append("\n");
//            }
//            reader.close();
//        }
//
//        return content.toString();
//    }
//
//    public <T> void runPermutations(PermutationStack permutations, PermutationTester permutationTester) {
//        Permutation permutation = new Permutation();
//
//        runPermutation(permutation, permutations, permutationTester);
//
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter(getAcceptedOutputFile()));
//            writer.write(runOutput.toString());
//            writer.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    private void runPermutation(Permutation permutation, PermutationStack permutations, PermutationTester permutationTester) {
//        Iterator<String> keyIterator = permutations.keySet().iterator();
//        if (keyIterator.hasNext()) {
//            String nextKey = keyIterator.next();
//            for (Object obj : permutations.get(nextKey)) {
//                permutation.pushEntry(nextKey, obj);
//
//                PermutationStack remainingPermutations = new PermutationStack(permutations);
//                remainingPermutations.remove(nextKey);
//
//                runPermutation(permutation, remainingPermutations, permutationTester);
//            }
//        } else {
//            try {
//                PermutationOutput testOutput = permutationTester.run(permutation);
//
//                runOutput.append(outputNextPermutation(permutation, lastPermutation, testOutput));
//
//                lastPermutation = permutation;
//
////                if (acceptedRuns.contains(permutation)) {
////                    throw new RuntimeException("TODO");
////                } else {
////                    //new permutation
////                }
//
////                for (String x : permutation.getCurrentValue())
////
////                System.out.println("---------------");
////                System.out.println(permutation.toString());
////
////                System.out.println("Data: ");
////                System.out.println(output.toString());
//
//            } catch (Exception e) {
//                throw new RuntimeException("Error running permutation:\n"+StringUtils.indent(permutation.toString(), 8), e);
//            }
//        }
//
//
//    }
//
//    protected String outputNextPermutation(Permutation thisPermutation, Permutation lastPermutation, PermutationOutput testOutput) {
//        StringBuilder returnValue  = new StringBuilder();
//        boolean foundStackDifference = false;
//        String[] parentStack = thisPermutation.getParentStack();
//        if (parentStack.length > 0) {
//            String[] lastPermutationStack = new String[0];
//            if (lastPermutation != null) {
//                lastPermutationStack = lastPermutation.getParentStack();
//            }
//            for (int i=0; i<parentStack.length; i++) {
//                if (foundStackDifference || lastPermutationStack.length - 1 < i || parentStack.length - 1 < i || !parentStack[i].equals(lastPermutationStack[i])) {
//                    foundStackDifference = true;
//                    returnValue.append(parentStack[i]).append("\n");
//                }
//            }
//        }
//
//        returnValue.append(thisPermutation.serialize(testOutput));
//
//        return returnValue.toString().trim();
//    }
//
//    public enum Type {
//        SQL
//    }


}
