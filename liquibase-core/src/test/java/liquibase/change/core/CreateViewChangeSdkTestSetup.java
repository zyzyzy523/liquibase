package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.diff.DiffResult;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.View;

import static junit.framework.TestCase.assertNotNull;

public class CreateViewChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws Exception {
        CreateViewChange change = (CreateViewChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
//        createTableChange.setCatalogName(change.getCatalogName());
//        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName("person");
        createTableChange.addColumn(new ColumnConfig().setName("id").setType("int"));
        createTableChange.addColumn(new ColumnConfig().setName("name").setType("varchar(255)"));

        return new Change[] {createTableChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        CreateViewChange change = (CreateViewChange) getChange();

        assertNotNull(diffResult.getUnexpectedObject(new View(change.getCatalogName(), change.getSchemaName(), change.getViewName()), getDatabase()));
    }
}
