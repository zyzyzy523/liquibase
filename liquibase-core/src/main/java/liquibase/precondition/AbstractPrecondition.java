package liquibase.precondition;

import liquibase.AbstractExtensibleObject;

public abstract class AbstractPrecondition extends AbstractExtensibleObject implements Precondition {

    @Override
    public int getPriority(String preconditionName) {
        if (preconditionName.equals(getName())) {
            return PRIORITY_DEFAULT;
        }
        return PRIORITY_NOT_APPLICABLE;
    }

}
