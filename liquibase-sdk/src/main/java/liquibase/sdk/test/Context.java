package liquibase.sdk.test;

import liquibase.change.Change;
import liquibase.change.ChangeFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

public class Context {

    private static Context instance = new Context();

    private Context() {
    }

    public static Context getInstance() {
        return instance;
    }

    public Class<? extends Change>[] getChangeClasses() {
        Set<Class> classes = new HashSet<Class>();
        for (Map.Entry<String, SortedSet<Class<? extends Change>>> entry : ChangeFactory.getInstance().getRegistry().entrySet()) {
            for (Class<? extends Change> clazz : entry.getValue()) {
                if (includeInContext(clazz)) {
                    classes.add(clazz);
                }
            }
        }

        return classes.toArray(new Class[classes.size()]);
    }

    protected boolean includeInContext(Class<? extends Change> clazz) {
        return !(
                clazz.getName().startsWith("liquibase") &&
                        (clazz.getName().contains(".core.") || clazz.getName().startsWith("liquibase.change.custom")));
    }
}