package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.diff.DiffResult;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.UniqueConstraint;

public class AddUniqueConstraintChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws Exception {
        AddUniqueConstraintChange change = (AddUniqueConstraintChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());

        for (String column : change.getColumnNames().split(",")) {
            createTableChange.addColumn(new ColumnConfig().setName(column.trim()).setType("int").setConstraints(new ConstraintsConfig().setNullable(false)));
        }
        createTableChange.addColumn(new ColumnConfig().setName("other_column").setType("int"));

        return new Change[] {createTableChange };

    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        AddUniqueConstraintChange change = (AddUniqueConstraintChange) getChange();

        diffResult.getUnexpectedObject(new UniqueConstraint(change.getConstraintName(), change.getCatalogName(), change.getSchemaName(), change.getTableName()), getDatabase());
    }
}