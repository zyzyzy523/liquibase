package liquibase.sdk.state;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.*;

import static org.junit.Assert.fail;

public class VerifyTest extends TestWatcher {
    private Class<?> testClass;
    private String testName;
    private String displayName;
    private List<Verification> checks;
    private SortedMap<String, Value> info;
    private SortedMap<String, Value> data;

//    @Override
    protected void starting(Description description) {
        this.testClass = description.getTestClass();
        this.testName = description.getMethodName();
        this.displayName = description.getDisplayName();

        this.info = new TreeMap<String, Value>();
        this.data = new TreeMap<String, Value>();
        this.checks = new ArrayList<Verification>();
    }

    @Override
    protected void succeeded(Description description) {
        if (info.size() > 0 || data.size() > 0) {
            if (info.size() == 0) {
                fail("Did not define any test state info to identify this permutation");
            }

            PersistedTestResults persistedTestResults = PersistedTestResults.getInstance(testClass, testName);

            persistedTestResults.runTest(this);
        }
    }

    @Override
    protected void failed(Throwable e, Description description) {
        PersistedTestResults.getInstance(testClass, testName).markFailed();
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    public String getTestName() {
        return testName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public SortedMap<String, Value> getInfo() {
        return Collections.unmodifiableSortedMap(info);
    }

    public void addInfo(String message, Object value) {
        this.addInfo(message, value, OutputFormat.DefaultFormat);
    }

    public void addInfo(String message, Object value, OutputFormat format) {
        this.info.put(message, new Value(value, format));
    }

    public void addOutput(String message, String value) {
        this.addData(message, value, OutputFormat.DefaultFormat);
    }

    public void addData(String message, Object value, OutputFormat outputFormat) {
        this.data.put(message, new Value(value, outputFormat));
    }

    public SortedMap<String, Value> getData() {
        return Collections.unmodifiableSortedMap(data);
    }

    public void verifyChanges(Verification check) {
        this.checks.add(check);
    }

    public List<Verification> getChecks() {
        return checks;
    }

    public static class Value {
        private Object value;
        private OutputFormat format;

        public Value(Object value, OutputFormat format) {
            this.value = value;
            this.format = format;
        }

        public String serialize() {
            return format.format(value);
        }
    }
}
