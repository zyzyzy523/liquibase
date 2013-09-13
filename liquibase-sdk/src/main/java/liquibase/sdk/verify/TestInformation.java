package liquibase.sdk.verify;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class TestInformation extends TestWatcher {
    private Class<?> testClass;
    private String testName;
    private String displayName;

    @Override
    protected void starting(Description description) {
        this.testClass = description.getTestClass();
        this.testName = description.getMethodName();
        this.displayName = description.getDisplayName();
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
}
