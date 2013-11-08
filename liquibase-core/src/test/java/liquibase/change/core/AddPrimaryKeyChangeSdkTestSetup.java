package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.diff.DiffResult;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Column;
import liquibase.structure.core.PrimaryKey;

import static junit.framework.Assert.assertNotNull;

public class AddPrimaryKeyChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws DatabaseException {
        AddPrimaryKeyChange change = (AddPrimaryKeyChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        for (String columnName : change.getColumnNames().split(",")) {
            createTableChange.addColumn(new ColumnConfig().setName(columnName.trim()).setType("int").setConstraints(new ConstraintsConfig().setNullable(false)));
        }
        createTableChange.addColumn(new ColumnConfig().setName("not_id").setType("int"));

        return new Change[] {createTableChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        AddPrimaryKeyChange change = (AddPrimaryKeyChange) getChange();

        PrimaryKey pk = diffResult.getUnexpectedObject(new PrimaryKey(change.getConstraintName(), change.getCatalogName(), change.getSchemaName(), change.getTableName(), change.getColumnNames().split(",")), getDatabase());
        assertNotNull(pk);
    }
}
