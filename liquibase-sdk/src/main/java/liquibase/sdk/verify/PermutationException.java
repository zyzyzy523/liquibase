package liquibase.sdk.verify;

public class PermutationException extends RuntimeException {
    public PermutationException() {
    }

    public PermutationException(String message) {
        super(message);
    }

    public PermutationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermutationException(Throwable cause) {
        super(cause);
    }
}
