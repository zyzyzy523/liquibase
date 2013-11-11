package liquibase.sdk.state;

import liquibase.util.StringUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.fail;

public class PersistedTestResults {

    private static final Map<String, PersistedTestResults> instances = new HashMap<String, PersistedTestResults>();

    private File acceptedOutputFile;
    private String testClass;
    private String testName;

    private boolean testsFailed = false;

    private Map<String, TestPermutation> acceptedRuns = new HashMap<String, TestPermutation>();
    private SortedMap<String, TestPermutation> newRuns = new TreeMap<String, TestPermutation>();

    private PersistedTestResults(String testClass, String testName) {
        this(testClass, testName, true);
    }

    protected PersistedTestResults(String testClass, String testName, boolean writeOnShutdown) {
        this.testClass = testClass;
        this.testName = testName;

        if (writeOnShutdown) {
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!testsFailed && (newRuns.size() > 0 || acceptedRuns.size() > 0)) {
                        writeAcceptedFile();
                    }
                }
            }));
        }

    }

    private void writeAcceptedFile() {
        File outputFile = getAcceptedOutputFile();
        try {
            if (foundDifferences()) {
                BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
                serialize(out);

                out.close();
            }
        } catch (IOException e) {
            System.out.println("Error writing output file "+outputFile);
        }
    }

    protected void serialize(BufferedWriter out) throws IOException {
        out.append("# Test Output: ").append(testClass).append(".").append(testName).append(" #\n\n");
        out.append("This output is generated when the test is ran.\n\n");

        for (Map.Entry<String, TestPermutation> iteration : newRuns.entrySet()) {
            out.append("## Permutation: "+iteration.getValue().getPermutationName()+" ##\n\n");
            out.append("- _VALIDATED:_ ").append(String.valueOf(iteration.getValue().isValidated())).append("\n");

            for (Map.Entry<String, VerifyTest.Value> entry : iteration.getValue().getPermutationDefinition().entrySet()) {
                appendMapEntry(entry, out);
            }

            out.append("\n");
            out.append("#### Permutation Information: ####\n");
            out.append("\n");

            for (Map.Entry<String, VerifyTest.Value> entry : iteration.getValue().getInfo().entrySet()) {
                appendMapEntry(entry, out);
            }

            out.append("\n");
            out.append("#### Permutation Data: ####\n");
            out.append("\n");

            for (Map.Entry<String, VerifyTest.Value> entry : iteration.getValue().getData().entrySet()) {
                appendMapEntry(entry, out);
            }

            out.append("\n\n");
        }

        out.flush();
    }

    private void appendMapEntry(Map.Entry<String, VerifyTest.Value> entry, BufferedWriter out) throws IOException {
        String value = entry.getValue().serialize();
        value  = value.replace("\r\n", "\n");

        boolean multiLine = value.contains("\n");

        out.append("- **").append(entry.getKey());

        if (multiLine) {
            out.append("~**\n");
        } else {
            out.append(":** ");
        }
        if (multiLine) {
            out.append(StringUtils.indent(value, 4));
        } else {
            out.append(value);
        }

        out.append("\n");
    }

    private boolean foundDifferences() {
        return true;
    }

    public void markFailed() {
        this.testsFailed = true;
    }

    public static PersistedTestResults getInstance(Class testClass, String testName) {
        String key = testClass.getName()+"#"+testName;
        if (!instances.containsKey(key)) {
            instances.put(key, new PersistedTestResults(testClass.getName(), testName));
        }
        return instances.get(key);
    }

    public File getAcceptedOutputFile() {
        if (acceptedOutputFile == null) {
            acceptedOutputFile = new File(getSavedStateDir(), testClass.replaceFirst(".*\\.","")+"."+testName+".accepted.md");
        }
        return acceptedOutputFile;
    }

    /**
     * Return the directory approved_output directory to use for this TestState. Creates approved_output dir if it does not exist
     */
    public File getSavedStateDir() {
        String testPackageDir = testClass.replaceFirst("\\..*?$", "").replace(".", "/");

        File sdkPropertiesFile = null;
        try {
            URL resource = getClass().getClassLoader().getResource("liquibase.sdk.properties");
            if (resource == null) {
                throw new RuntimeException("Could not find liquibase.sdk.properties");
            }
            sdkPropertiesFile = new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        String returnDir = sdkPropertiesFile.getAbsolutePath().replace("\\", "/") //normalize separators
                .replaceFirst("/liquibase.sdk.properties$", "") //strip off file name
                .replace("target/test-classes", "src/test/java"); //switch to src dir
        File file = new File(returnDir + "/" + testPackageDir);
        file.mkdirs();
        if (!file.exists()) {
            throw new RuntimeException("Expected source path " + file.getAbsolutePath() + " does not exist");
        }

        file = new File(file, "approved_output");
        file.mkdir();
        return file;
    }

    public void write() throws Exception {
        BufferedWriter outputStream = new BufferedWriter(new FileWriter(getAcceptedOutputFile()));
//        outputStream.write(stateContent.toString());
        outputStream.flush();
        outputStream.close();
    }


    public void runTest(VerifyTest run) {
        TestPermutation thisRun = new TestPermutation(run);

        String thisRunKey = thisRun.getKey();
        if (newRuns.containsKey(thisRunKey)) {
            fail("Found multiple permutations that have the same info: "+ thisRunKey);
        }

        Object acceptedRun = this.acceptedRuns.get(thisRunKey);

        if (acceptedRun == null) {
            try {
                for (Setup setup : run.getSetupCommands()) {
                    String result = null;
                    try {
                        result = setup.setup();
                    } catch (Exception e) {
                        e.printStackTrace();
                        fail("Setup threw exception: "+e.getMessage());
                    }
                    if (result != null) {
                        fail("Setup failed: " + result);
                    }
                }

                Verification.Result finalResult = Verification.Result.PASSED;
                if (run.getChecks().size() == 0) {
                    finalResult = Verification.Result.CANNOT_VALIDATE;
                } else {
                    for (Verification check : run.getChecks()) {
                        try {
                            Verification.Result result = check.check();
                            if (result == Verification.Result.CANNOT_VALIDATE) {
                                finalResult = Verification.Result.CANNOT_VALIDATE;
                            }
                        } catch (AssertionError e) {
                            throw run.getFailureHandler().toAssertionError(e);
                        } catch (Exception e) {
                            throw run.getFailureHandler().toAssertionError(e);
                        }
                    }
                }

                thisRun.setValidated(finalResult == Verification.Result.PASSED);

                newRuns.put(thisRunKey, thisRun);
            } finally {
                for (Cleanup cleanup : run.getCleanupCommands()) {
                    try {
                        cleanup.cleanup();
                    } catch (Exception e) {
                        System.out.println("Error in cleanup: "+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } else {
            throw new RuntimeException("todo");
        }
    }

    protected void copyAcceptedRunsToNewRuns() {
        this.newRuns.putAll(this.acceptedRuns);
    }

    public static PersistedTestResults parse(BufferedReader fileContents) throws IOException {
        return new PersistedTestResultsParser().parse(fileContents);
    }

    protected void addAcceptedRun(TestPermutation currentPermutation) {
        acceptedRuns.put(currentPermutation.getKey(), currentPermutation);
    }
}
