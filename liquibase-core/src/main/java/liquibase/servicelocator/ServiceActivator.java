package liquibase.servicelocator;

/**
 * By default, defined services are automatically loaded and instantiated but that doesn't always work.
 * Sometimes you cannot expect classes to always exist in the classpath (such as hibernate/spring classes) or special configuration needs to be done.
 * <br><br>
 * To handle times like this, services can implement this interface. When {@link ServiceLocator} finds a service that implements this, it will call {@link #activate()}
 * and add the instance returned by that method to the registry of services.
 *
 */
public interface ServiceActivator<Type> {

    /**
     * Returns the actual instance to add to the registry of services.
     * Return null if this service cannot be activated.
     */
    Type activate();
}
