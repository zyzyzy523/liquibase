package liquibase.sdk.state;

public abstract class FailureHandler {
    public AssertionError toAssertionError(Throwable e) {
        getStateMessage();
        return new AssertionError(e);
    }

    public abstract String getStateMessage();
}
