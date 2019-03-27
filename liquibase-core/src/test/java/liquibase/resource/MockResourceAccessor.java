package liquibase.resource;

import liquibase.AbstractExtensibleObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

public class MockResourceAccessor extends AbstractExtensibleObject implements ResourceAccessor {

    private Map<String, List<byte[]>> dataByPath = new HashMap<>();

    public MockResourceAccessor() {
    }

    public MockResourceAccessor(Map<String, String> dataByPath) {
        for (Map.Entry<String, String> entry : dataByPath.entrySet()) {
            this.addResource(entry.getKey(), String.valueOf(entry.getValue()));
        }
    }

    public MockResourceAccessor addMockXsd(String rootElement) {
        this.addResource("liquibase/parser/xml/mock.xsd", "<xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema' "+
        "targetNamespace='http://www.liquibase.org/xml/ns/mock' xmlns='http://www.liquibase.org/xml/ns/mock' "+
        "elementFormDefault='qualified'>\n"+
        "<xsd:element name='"+rootElement+"'/>\n"+
        "</xsd:schema>");

        return this;
    }

    @Override
    public InputStreamList openStreams(String relativeTo, String streamPath) throws IOException {
        List<byte[]> list = dataByPath.get(streamPath);
        if (list == null || list.size() == 0) {
            return null;
        } else {
            InputStreamList returnList = new InputStreamList();
            for (byte[] data : list) {
                returnList.add(URI.create("mock:"+relativeTo), new ByteArrayInputStream(data));
            }
            return returnList;
        }
    }

    @Override
    public InputStream openStream(String relativeTo, String streamPath) throws IOException {
        InputStreamList streams = openStreams(relativeTo, streamPath);
        if (streams == null || streams.size() == 0) {
            return null;
        } else if (streams.size() > 1) {
            throw new IOException("Multiples streams matched "+streamPath);
        }
        return streams.iterator().next();
    }

    @Override
    public SortedSet<String> list(String relativeTo, String path, boolean recursive, boolean includeFiles, boolean includeDirectories) throws IOException {
        return null;
    }

    public MockResourceAccessor addResource(String path, String data) {
        List<byte[]> dataList = dataByPath.computeIfAbsent(path, k -> new ArrayList<>());

        dataList.add(data.getBytes());

        return this;
    }
}
