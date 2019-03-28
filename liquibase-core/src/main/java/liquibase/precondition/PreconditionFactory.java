package liquibase.precondition;

import liquibase.Scope;
import liquibase.change.Change;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.plugin.AbstractPluginFactory;
import liquibase.plugin.Plugin;
import liquibase.servicelocator.ServiceLocator;

import java.util.*;

public class PreconditionFactory extends AbstractPluginFactory<Precondition> {

    protected PreconditionFactory() {
    }

    @Override
    protected Class<Precondition> getPluginClass() {
        return Precondition.class;
    }

    @Override
    protected int getPriority(Precondition precondition, Object... args) {
        return precondition.getPriority((String) args[0]);
    }

    /**
     * Create a new Precondition subclass based on the given tag name.
     */
    public Precondition getPrecondition(String tagName) {
        return (Precondition) getPlugin(tagName).clone();
    }

    /**
     * Returns all defined preconditions. Returned set is not modifiable.
     */
    public SortedSet<String> getNames() {
        SortedSet<String> names = new TreeSet<>();
        for (Precondition precondition: findAllInstances()) {
            names.add(precondition.getName());
        }
        return Collections.unmodifiableSortedSet(names);
    }

}
