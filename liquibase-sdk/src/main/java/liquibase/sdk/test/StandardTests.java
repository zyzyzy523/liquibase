package liquibase.sdk.test;

import liquibase.sdk.Context;
import org.junit.Test;

import static org.junit.Assert.fail;

public class StandardTests {


    @Test
    public void allFoundClassesAreRegistered() {
//        for (Class clazz : Context.getInstance().getChangeClasses()) {
//            try {
//                clazz.newInstance();
//            } catch (Throwable e) {
//                fail("Error instantiating "+clazz.getName()+", extension classes need a no-arg constructor: "+e.getMessage());
//            }
//        }
    }


}
