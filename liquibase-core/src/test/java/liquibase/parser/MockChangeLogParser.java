package liquibase.parser;

import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.ChangeLog;
import liquibase.exception.ChangeLogParseException;
import liquibase.servicelocator.LiquibaseService;

@LiquibaseService(skip = true)
public class MockChangeLogParser implements ChangeLogParser {

    private String[] validExtensions;

    public MockChangeLogParser(String... validExtensions) {
        this.validExtensions = validExtensions;
    }

    @Override
    public int getPriority() {
        return PRIORITY_DEFAULT;
    }

    @Override
    public boolean supports(String changeLogFile) {
        for (String ext : validExtensions) {
            if (changeLogFile.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ChangeLog parse(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters) throws ChangeLogParseException {
        return null;
    }
}
