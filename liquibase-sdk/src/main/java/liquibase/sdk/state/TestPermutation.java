package liquibase.sdk.state;

import java.util.SortedMap;
import java.util.TreeMap;

public class TestPermutation {

    private String permutationName;
    private String key;
    private SortedMap<String, VerifyTest.Value> data;
    private SortedMap<String,VerifyTest.Value> permutationDefinition;
    private SortedMap<String,VerifyTest.Value> info;
    private Boolean validated;

    public TestPermutation(VerifyTest run) {
        this(run.getPermutationName());
        this.permutationDefinition = run.getPermutationDefinition();
        this.info = run.getInfo();
        this.data = run.getData();
    }

    public TestPermutation(String permutationName) {
        this.permutationName = permutationName;
        this.key = permutationName;

        this.permutationDefinition = new TreeMap<String, VerifyTest.Value>();
        this.info= new TreeMap<String, VerifyTest.Value>();
        this.data = new TreeMap<String, VerifyTest.Value>();
    }

    public String getKey() {
        return key;
    }

    public String getPermutationName() {
        return permutationName;
    }

    public SortedMap<String, VerifyTest.Value> getData() {
        return data;
    }

    public void addData(String key, Object value, OutputFormat outputFormat) {
        this.data.put(key, new VerifyTest.Value(value, outputFormat));
    }

    public SortedMap<String, VerifyTest.Value> getPermutationDefinition() {
        return permutationDefinition;
    }

    public void addDefinition(String key, Object value, OutputFormat outputFormat) {
        this.permutationDefinition.put(key, new VerifyTest.Value(value, outputFormat));
    }

    public SortedMap<String, VerifyTest.Value> getInfo() {
        return info;
    }

    public void addInfo(String key, Object value, OutputFormat outputFormat) {
        this.info.put(key, new VerifyTest.Value(value, outputFormat));
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public boolean isValidated() {
        return validated;
    }
}
