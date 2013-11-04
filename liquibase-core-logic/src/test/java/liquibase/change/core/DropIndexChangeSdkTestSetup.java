package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.diff.DiffResult;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Index;

import static junit.framework.TestCase.assertNotNull;

public class DropIndexChangeSdkTestSetup extends StandardChangeSdkTestSetup {

    @Override
    protected Change[]  prepareDatabase() throws Exception {
        DropIndexChange change = (DropIndexChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());

        createTableChange.addColumn(new ColumnConfig().setName("name").setType("varchar(255)").setConstraints(new ConstraintsConfig().setNullable(false)));
        createTableChange.addColumn(new ColumnConfig().setName("other_column").setType("int"));

        CreateIndexChange createIndexChange = new CreateIndexChange();
        createIndexChange.setIndexName(change.getIndexName());
        createIndexChange.setTableName(change.getTableName());
        createIndexChange.addColumn(new ColumnConfig().setName("name"));

        return new Change[] {createTableChange, createIndexChange };

    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        DropIndexChange change = (DropIndexChange) getChange();

        assertNotNull(diffResult.getMissingObject(new Index(change.getIndexName()), getDatabase()));
    }
}
