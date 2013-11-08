package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.diff.DiffResult;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.View;

import static junit.framework.TestCase.assertNotNull;

public class DropViewChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws DatabaseException {
        DropViewChange change = (DropViewChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName("person");
        createTableChange.addColumn(new ColumnConfig().setName("id").setType("int"));
        createTableChange.addColumn(new ColumnConfig().setName("name").setType("varchar(20)"));

        CreateViewChange createViewChange = new CreateViewChange();
        createViewChange.setCatalogName(change.getCatalogName());
        createViewChange.setSchemaName(change.getSchemaName());
        createViewChange.setViewName(change.getViewName());
        createViewChange.setSelectQuery("select * from person");

        return new Change[] {createTableChange, createViewChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        DropViewChange change = (DropViewChange) getChange();

        assertNotNull(diffResult.getMissingObject(new View(change.getCatalogName(), change.getSchemaName(), change.getViewName()), getDatabase()));
    }
}
