package liquibase.sdk.supplier.resource;

import liquibase.resource.ResourceAccessor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

public class Resources {

    public static final liquibase.resource.ResourceAccessor RESOURCE_ACCESSOR = new ResourceAccessor();

    private static final String usersCsv = "username, fullname, pk_id\n" +
            "nvoxland, Nathan Voxland, 1\n" +
            "bob, Bob Bobson, 2";

    private static class ResourceAccessor implements liquibase.resource.ResourceAccessor {



        @Override
        public InputStream getResourceAsStream(String file) throws IOException {
            if (file.toLowerCase().endsWith("csv")) {
                return new ByteArrayInputStream(usersCsv.getBytes());
            } else {
                throw new RuntimeException("Unknown resource type: "+file);
            }
        }

        @Override
        public Enumeration<URL> getResources(String packageName) throws IOException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ClassLoader toClassLoader() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
