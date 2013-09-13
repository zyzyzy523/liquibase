package liquibase.sdk.verify;

import liquibase.database.Database;

import javax.xml.crypto.NodeSetData;
import java.util.*;

public class PermutationStack {

    private LinkedHashMap<String, SortedSet> permutations = new LinkedHashMap<String, SortedSet>();

    public PermutationStack() {
    }

    public PermutationStack(PermutationStack permutations) {
        this.permutations.putAll(permutations.permutations);
    }

    public <T> void push(String key, Collection<T> values, Class<T> compareType) {
        Comparator comparator;
        if (compareType.equals(Class.class)) {
            comparator = new Comparator<Class>() {
                @Override
                public int compare(Class o1, Class o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            };
        } else if (compareType.equals(Database.class)) {
            comparator = new Comparator<Database>() {
                @Override
                public int compare(Database o1, Database o2) {
                    return o1.getShortName().compareTo(o2.getShortName());
                }
            };
        } else {
            throw new RuntimeException("No default comparator for "+compareType.getName()+". Call push with a passed comparator");
        }

        push(key, values, comparator);
    }

    public <T> void push(String key, Collection<T> values, Comparator<T> comparator) {
        TreeSet<T> sortedValues = new TreeSet<T>(comparator);
        sortedValues.addAll(values);
        permutations.put(key, sortedValues);
    }


    public Set keySet() {
        return permutations.keySet();
    }

    public Collection get(String key) {
        return permutations.get(key);
    }

    public void remove(String key) {
        permutations.remove(key);
    }
}
