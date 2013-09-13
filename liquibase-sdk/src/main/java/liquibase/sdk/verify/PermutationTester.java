package liquibase.sdk.verify;

public interface PermutationTester {
    public PermutationOutput run(Permutation permutation) throws Exception;
}
