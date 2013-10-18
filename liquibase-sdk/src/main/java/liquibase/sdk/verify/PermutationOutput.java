package liquibase.sdk.verify;

import liquibase.sdk.state.OutputFormat;
import liquibase.util.StringUtils;

import java.util.Map;
import java.util.TreeMap;

public class PermutationOutput {

//    private Map<String, OutputFormat.OutputData> data = new TreeMap<String, OutputFormat.OutputData>();
//
//    public void set(String key, Object value, OutputFormat formatter) {
//        data.put(key, new OutputFormat.OutputData(value, formatter));
//    }
//
//    public void set(String key, String value) {
//        set(key, value, OutputFormat.DefaultFormat);
//    }
//
//    public <T> T get(String key, Class<T> returnType) {
//        return (T) data.get(key).value;
//    }
//
//    public String toString() {
//        StringBuilder out = new StringBuilder();
//        for (Map.Entry<String, OutputFormat.OutputData> entry : data.entrySet()) {
//            OutputFormat.OutputData value = entry.getValue();
//            String formattedValue;
//            if (value.value == null) {
//                formattedValue = "NULL";
//            } else {
//                formattedValue = value.formatter.format(value.value);
//            }
//            out.append("**").append(entry.getKey()).append("**\n\n").append(StringUtils.indent(formattedValue, 4)).append("\n\n");
//        }
//
//        String outString = StringUtils.trimToNull(out.toString().trim());
//
//        if (outString == null) {
//            outString = "**NO PERMUTATION DATA**";
//        }
//
//
//        return outString;
//    }
//
//    public String serialize() {
//        return toString();
//    }
}
