package liquibase.sdk.state;

public interface Verification {
    public Result check() throws Exception;

    public enum Result {
        PASSED,
        CANNOT_VALIDATE
    }
}
