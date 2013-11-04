package liquibase.sdk.standardtests.change;

import liquibase.CatalogAndSchema;
import liquibase.change.Change;
import liquibase.database.Database;
import liquibase.diff.DiffGeneratorFactory;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.sdk.state.Cleanup;
import liquibase.sdk.state.FailureHandler;
import liquibase.sdk.state.Setup;
import liquibase.sdk.state.Verification;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.SnapshotControl;
import liquibase.snapshot.SnapshotGeneratorFactory;
import liquibase.statement.SqlStatement;
import liquibase.util.StringUtils;

public abstract class StandardChangeSdkTestSetup implements Setup, Verification, Cleanup {

    private Change change;
    private Database database;
    private DatabaseSnapshot baseSnapshot;
    private DatabaseSnapshot finalSnapshot;

    public Change getChange() {
        return change;
    }

    public void setChange(Change change) {
        this.change = change;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    @Override
    public final String setup() throws Exception {
        if (database.getConnection() == null) {
            return null;
        }
        execute(prepareDatabase());

        this.baseSnapshot = SnapshotGeneratorFactory.getInstance().createSnapshot(CatalogAndSchema.DEFAULT, getDatabase(), createSnapshotControl());

        return null;
    }

    protected abstract Change[]  prepareDatabase() throws Exception;

    protected abstract void checkDiffResult(DiffResult diffResult) throws Exception;

    protected void execute(SqlStatement... statements) throws DatabaseException {
        if (database.getConnection() == null) {
            return;
        }
        if (statements == null) {
            return;
        }
        for (SqlStatement statement : statements) {
            ExecutorService.getInstance().getExecutor(database).execute(statement);
        }

    }

    protected void execute(Change... changes) throws DatabaseException {
        if (database.getConnection() == null) {
            return;
        }
        if (changes == null) {
            return;
        }
        for (Change change : changes) {
            ExecutorService.getInstance().getExecutor(database).execute(change);
        }
    }

    @Override
    public Result check() throws Exception {
        if (database.getConnection() == null) {
            return Result.CANNOT_VALIDATE;
        }

        SnapshotControl snapshotControl = createSnapshotControl();
        this.finalSnapshot = SnapshotGeneratorFactory.getInstance().createSnapshot(CatalogAndSchema.DEFAULT, getDatabase(), snapshotControl);

        DiffResult diffResult = DiffGeneratorFactory.getInstance().compare(this.baseSnapshot, this.finalSnapshot, new CompareControl(snapshotControl.getTypesToInclude()));
        checkDiffResult(diffResult);

        return Result.CANNOT_VALIDATE;
    }

    protected SnapshotControl createSnapshotControl() {
        return new SnapshotControl(getDatabase());
    }

    @Override
    public void cleanup() throws Exception {
        if (getDatabase().getConnection() == null) {
            return;
        }
        getDatabase().dropDatabaseObjects(CatalogAndSchema.DEFAULT);
    }
}
