package liquibase.ext.hibernate.database;

import liquibase.database.AbstractDatabaseActivator;
import org.hibernate.Hibernate;
import org.springframework.core.io.Resource;

public class HibernateSpringBeanDatabaseActivator extends AbstractDatabaseActivator<HibernateSpringBeanDatabase> {

    @Override
    public Class<HibernateSpringBeanDatabase> getClassToActivate() {
        return HibernateSpringBeanDatabase.class;
    }

    @Override
    public Class[] getRequiredClasses() {
        return new Class[]{
                Hibernate.class,
                Resource.class,
        };
    }
}
