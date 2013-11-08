package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.diff.DiffResult;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.UniqueConstraint;

import static junit.framework.Assert.assertNotNull;

public class DropUniqueConstraintChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws Exception {
        DropUniqueConstraintChange change = (DropUniqueConstraintChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        String uniqueColumns = change.getUniqueColumns();

        if (uniqueColumns == null) {
            uniqueColumns = "test_col";
        }

        for (String column : uniqueColumns.split(",")) {
            createTableChange.addColumn(new ColumnConfig().setName(column.trim()).setType("int").setConstraints(new ConstraintsConfig().setNullable(false)));
        }
        createTableChange.addColumn(new ColumnConfig().setName("other_column").setType("int"));

        AddUniqueConstraintChange addUniqueConstraintChange = new AddUniqueConstraintChange();
        addUniqueConstraintChange.setCatalogName(change.getCatalogName());
        addUniqueConstraintChange.setSchemaName(change.getSchemaName());
        addUniqueConstraintChange.setTableName(change.getTableName());
        addUniqueConstraintChange.setColumnNames(uniqueColumns);
        addUniqueConstraintChange.setConstraintName(change.getConstraintName());

        return new Change[] {createTableChange, addUniqueConstraintChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        DropUniqueConstraintChange change = (DropUniqueConstraintChange) getChange();

        String[] columns = null;
        if (change.getUniqueColumns() != null) {
            columns = change.getUniqueColumns().split(",");
        }

        assertNotNull(diffResult.getMissingObject(new UniqueConstraint(change.getConstraintName(), change.getCatalogName(), change.getSchemaName(), change.getTableName(), columns), getDatabase()));
    }
}
