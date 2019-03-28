package liquibase.parser.json;

import com.google.gson.*;
import liquibase.Scope;
import liquibase.exception.LiquibaseException;
import liquibase.exception.ParseException;
import liquibase.parser.ParsedNode;
import liquibase.parser.yaml.AbstractParsedNodeParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Parses json files using GSON.
 *
 * @see ParsedNodeTypeAdapter
 */
public class JsonParser extends AbstractParsedNodeParser {

    @Override
    public int getPriority(String relativeTo, String path, Class objectType) {
        if ((path.toLowerCase().endsWith(".json"))) {
            return PRIORITY_DEFAULT;
        }
        return PRIORITY_NOT_APPLICABLE;
    }

    @Override
    public ParsedNode parseToParsedNode(String relativeTo, String path, Class objectType) throws ParseException {
        try (InputStream inputStream = Scope.getCurrentScope().getResourceAccessor().openStream(relativeTo, path)) {
            if (inputStream == null) {
                throw new ParseException("Could not find file to parse: " + path + (relativeTo != null ? " relative to " + relativeTo : ""), null);
            }

            try (Reader reader = new InputStreamReader(inputStream)) {
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .registerTypeAdapter(ParsedNode.class, new ParsedNodeTypeAdapter())
                        .create();

                ParsedNode node = null;
                try {
                    node = gson.fromJson(reader, ParsedNode.class);
                } catch (JsonSyntaxException | JsonIOException e) {
                    throw new ParseException(e, null);
                }

                node.addChild("physicalPath").setValue(path);
                return node;
            }
        } catch (ParseException e) {
            throw e;
        } catch (LiquibaseException | IOException e) {
            throw new ParseException(e, null);
        }
    }

    @Override
    public String describeOriginal(ParsedNode parsedNode) {
        return new JsonParser().describeOriginal(parsedNode);
    }
}
