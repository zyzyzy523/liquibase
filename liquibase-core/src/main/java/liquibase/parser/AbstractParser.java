package liquibase.parser;

import liquibase.parser.structureddata.mapping.ParsedNodeMappingFactory;
import liquibase.parser.structureddata.postprocessor.MappingPostprocessor;
import liquibase.parser.structureddata.postprocessor.MappingPostprocessorFactory;
import liquibase.parser.structureddata.preprocessor.ParsedNodePreprocessor;
import liquibase.parser.structureddata.preprocessor.ParsedNodePreprocessorFactory;
import liquibase.plugin.AbstractPlugin;

/**
 * Convenience base class for {@link Parser} implementations.
 */
public abstract class AbstractParser extends AbstractPlugin implements Parser {

}
