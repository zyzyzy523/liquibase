package liquibase.parser;

import liquibase.Scope;
import liquibase.exception.DependencyException;
import liquibase.exception.ParseException;
import liquibase.parser.mapping.ParsedNodeMappingFactory;
import liquibase.parser.postprocessor.MappingPostprocessor;
import liquibase.parser.postprocessor.MappingPostprocessorFactory;
import liquibase.parser.preprocessor.ParsedNodePreprocessor;
import liquibase.parser.preprocessor.ParsedNodePreprocessorFactory;
import liquibase.plugin.AbstractPlugin;
import liquibase.util.StringUtil;

/**
 * Convenience base class for {@link Parser} implementations.
 */
public abstract class AbstractParser extends AbstractPlugin implements Parser {

}
