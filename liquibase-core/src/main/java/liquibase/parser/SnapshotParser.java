package liquibase.parser;

import liquibase.exception.LiquibaseParseException;
import liquibase.resource.ResourceAccessor;
import liquibase.servicelocator.PrioritizedService;
import liquibase.snapshot.DatabaseSnapshot;

public interface SnapshotParser extends PrioritizedService {

    public DatabaseSnapshot parse(String path, ResourceAccessor resourceAccessor) throws LiquibaseParseException;

    boolean supports(String path, ResourceAccessor resourceAccessor);

}
