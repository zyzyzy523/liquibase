package liquibase.parser;

import liquibase.ExtensibleObject;
import liquibase.SingletonObject;
import liquibase.exception.ParseException;
import liquibase.plugin.Plugin;

import java.io.OutputStream;

/**
 * Unparsers take an object and into an OutputStream.
 *
 * @see AbstractUnparser
 * @see UnparserFactory
 */
public interface Unparser extends Plugin, ExtensibleObject, SingletonObject {

    int getPriority(String path);

    /**
     * Converts the object to the given outputStream.
     * The passed outputPath is used to determine the unparser to use, so it must be set, even though all writing must go to the outputStream.
     * <p/>
     * Does nothing if object is null.
     */
    void unparse(Object object, String outputPath, OutputStream outputStream) throws ParseException;


}
