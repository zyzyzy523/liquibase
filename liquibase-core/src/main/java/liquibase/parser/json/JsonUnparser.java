package liquibase.parser.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import liquibase.exception.ParseException;
import liquibase.parser.structureddata.AbstractStructuredDataUnparser;
import liquibase.parser.ParsedNode;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Parses json files using GSON.
 *
 * @see ParsedNodeTypeAdapter
 */
public class JsonUnparser extends AbstractStructuredDataUnparser {

    @Override
    public int getPriority(String path) {
        if (path.toLowerCase().endsWith(".json")) {
            return PRIORITY_DEFAULT;
        } else {
            return PRIORITY_NOT_APPLICABLE;
        }
    }

    @Override
    public void unparse(ParsedNode node, OutputStream output) throws ParseException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ParsedNode.class, new ParsedNodeTypeAdapter())
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        try (OutputStreamWriter writer = new OutputStreamWriter(output)) {
            gson.toJson(node, writer);
        } catch (IOException e) {
            throw new ParseException(e, null);
        }

    }


}
