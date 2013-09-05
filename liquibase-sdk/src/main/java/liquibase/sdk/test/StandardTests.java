package liquibase.sdk.test;

import liquibase.change.Change;
import liquibase.util.StringUtils;
import org.apache.commons.cli.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.fail;

public class StandardTests {


    @Test
    public void canInstantiate() {
        for (Class clazz : Context.getInstance().getChangeClasses()) {
            try {
                clazz.newInstance();
            } catch (Throwable e) {
                fail("Error instantiating "+clazz.getName()+", extension classes need a no-arg constructor: "+e.getMessage());
            }
        }
    }


}
