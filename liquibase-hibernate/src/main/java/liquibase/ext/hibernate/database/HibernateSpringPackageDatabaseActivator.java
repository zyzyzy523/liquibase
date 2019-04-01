package liquibase.ext.hibernate.database;

import liquibase.database.AbstractDatabaseActivator;
import org.hibernate.Hibernate;
import org.springframework.core.io.Resource;

public class HibernateSpringPackageDatabaseActivator extends AbstractDatabaseActivator<HibernateSpringPackageDatabase> {

    @Override
    public Class<HibernateSpringPackageDatabase> getClassToActivate() {
        return HibernateSpringPackageDatabase.class;
    }

    @Override
    public Class[] getRequiredClasses() {
        return new Class[]{
                Hibernate.class,
                Resource.class,
        };
    }
}
