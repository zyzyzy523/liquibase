package liquibase.sdk.verify;

import liquibase.sdk.state.OutputFormat;
import liquibase.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Permutation {

//    private LinkedHashMap<String, OutputFormat.OutputData> stack = new LinkedHashMap<String, OutputFormat.OutputData>();
//
//    protected void setCurrentValue(String key, Object value, OutputFormat outputFormat) {
//        stack.put(key, new OutputFormat.OutputData(value, outputFormat));
//    }
//
//    protected void pushEntry(String key, Object value) {
//        setCurrentValue(key, value, OutputFormat.DefaultFormat);
//    }
//
//    public <T> T getCurrentValue(String key, Class<T> returnType) {
//        return (T) stack.get(key).value;
//    }
//
//    /**
//     * Returns an the Permutation stack excluding the the current level.
//     * @return
//     */
//    public String[] getParentStack() {
//        if (this.stack.size() < 2) {
//            return new String[0];
//        }
//
//        String[] returnStack = new String[this.stack.size() - 1];
//        int i = 0;
//
//        for (Map.Entry<String, OutputFormat.OutputData> entry : stack.entrySet()) {
//            if (i >= returnStack.length) {
//                continue;
//            }
//
//            returnStack[i] = StringUtils.repeat("#", i+1)+" "+entry.getKey();
//            if (entry.getValue().value != null) {
//                returnStack[i] += ": "+entry.getValue().toString();
//            }
//            i++;
//        }
//
//        return returnStack;
//    }
//
//    @Override
//    public String toString() {
//        StringBuffer buff = new StringBuffer();
//        for (String key : stack.keySet()) {
//            buff.append(key).append(" => ").append(stack.get(key)).append("\n");
//        }
//        return buff.toString();
//    }
//
//    public String serialize(PermutationOutput output) {
//        StringBuilder out = new StringBuilder();
//
//        int level = this.stack.size();
//        String currentPermutationKey = new ArrayList<String>(this.stack.keySet()).get(this.stack.size() - 1);
//        Object currentPermutationValue = stack.get(currentPermutationKey);
//        out.append(StringUtils.repeat("#", level)).append(" ").append(currentPermutationKey);
//        if (currentPermutationValue != null) {
//            out.append(": ").append(currentPermutationValue);
//        }
//
//        out.append("\n").append("\n*****\n\n");
//
//        out.append(output.serialize());
//
//        return out.toString().trim();
//    }
}
