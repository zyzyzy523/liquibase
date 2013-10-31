package liquibase.change.core;

import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class DropUniqueConstraintChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws Exception {
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

        execute(createTableChange, addUniqueConstraintChange);

        return null;

    }
}
