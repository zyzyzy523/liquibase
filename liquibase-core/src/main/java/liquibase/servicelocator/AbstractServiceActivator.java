package liquibase.servicelocator;

import liquibase.Scope;

/**
 * Convenience class for {@link ServiceActivator} services
 */
public abstract class AbstractServiceActivator<ClassToActivate> implements ServiceActivator<ClassToActivate> {

    @Override
    public ClassToActivate activate() {
        if (!canActivate()) {
            return null;
        }

        return createService();
    }

    protected ClassToActivate createService() {
        try {
            return getClassToActivate().newInstance();
        } catch (Throwable e) {
            Scope.getCurrentScope().getLog(getClass()).info("Could not instantiate " + getClass().getName() + ": " + e.getMessage(), e);
            return null;
        }
    }

    protected boolean canActivate() {
        try {
            for (Class requiredClass : getRequiredClasses()) {
                Scope.getCurrentScope().getLog(getClass()).info("Required class for " + getClass().getName() + ": " + requiredClass.getName() + " found");
            }
        } catch (NoClassDefFoundError e) {
            Scope.getCurrentScope().getLog(getClass()).info("Could not activate " + getClass().getName() + ": " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    public abstract Class<ClassToActivate> getClassToActivate();

    public abstract Class[] getRequiredClasses();
}
