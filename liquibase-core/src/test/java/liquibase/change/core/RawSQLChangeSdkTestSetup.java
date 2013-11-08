package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.diff.DiffResult;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.statement.core.RawSqlStatement;

import static org.junit.Assert.assertTrue;

public class RawSQLChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws DatabaseException {
        RawSQLChange change = (RawSQLChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
//        createTableChange.setCatalogName(change.getCatalogName());
//        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName("person");
        createTableChange.addColumn(new ColumnConfig().setName("name").setType("varchar(10)"));
        createTableChange.addColumn(new ColumnConfig().setName("address").setType("varchar(10)"));

        return new Change[] {createTableChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) throws Exception {
        //todo generic check

        int rows = ExecutorService.getInstance().getExecutor(getDatabase()).queryForInt(new RawSqlStatement("select count(*) from person"));
        assertTrue(rows > 0);

    }

}
