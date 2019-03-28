package liquibase.parser;

import liquibase.changelog.ChangeLog;
import liquibase.changelog.ChangeLogParameters;
import liquibase.exception.ChangeLogParseException;

/**
 * Defines the methods a class which is able to parse a Liquibase changelog file must support. Different parsers
 * are able to parse different file formats (e.g. XML, YAML, JSON, ...)
 */
public interface ChangeLogParser extends LiquibaseParser {
    
    /**
     * Parses a Liquibase database changelog and returns the parsed form as an object.
     * @param physicalChangeLogLocation the physical location of the changelog. The exakt file formats and locations
     * where can load changelog files from depend on the implementations and capabilities of the implementing parsers.
     * @param changeLogParameters parameters given by the end user that should be applied while parsing the changelog
     *  (i.e. replacement of ${placeholders} inside the changelogs with user-defined content)
     * @return the parsed ChangeLog in object form
     * @throws ChangeLogParseException if an error occurs during parsing of the ChangeLog
     */
    ChangeLog parse(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters) throws ChangeLogParseException;
    
    /**
     * Checks if the file format is supported by an implementing ChangeLogParser and returns true if that is the case.
     * @param changeLogFile the location of the changelog file
     * @return true if the file format is supported, false if it is not.
     */
    boolean supports(String changeLogFile);
}
