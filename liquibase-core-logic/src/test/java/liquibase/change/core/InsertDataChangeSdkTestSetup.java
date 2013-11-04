package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.diff.DiffResult;
import liquibase.diff.ObjectDifferences;
import liquibase.executor.ExecutorService;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.snapshot.SnapshotControl;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.core.Data;

import java.util.Map;

import static org.junit.Assert.assertTrue;

public class InsertDataChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws Exception {
        InsertDataChange change = (InsertDataChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        for (ColumnConfig column : change.getColumns()) {
            createTableChange.addColumn(column);
        }

        return new Change[] {createTableChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) throws Exception {
        InsertDataChange change = (InsertDataChange) getChange();

        int rows = ExecutorService.getInstance().getExecutor(getDatabase()).queryForInt(new RawSqlStatement("select count(*) from " + getDatabase().escapeTableName(change.getCatalogName(), change.getSchemaName(), change.getTableName())));
        assertTrue(rows > 0);
    }
}
