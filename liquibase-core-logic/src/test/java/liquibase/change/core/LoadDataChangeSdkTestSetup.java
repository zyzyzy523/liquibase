package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.diff.DiffResult;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class LoadDataChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws Exception {
        LoadDataChange change = (LoadDataChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        for (ColumnConfig column : change.getColumns()) {
            createTableChange.addColumn(column);
        }
        for (String columnName : change.getCSVReader().readNext()) {
            createTableChange.addColumn(new ColumnConfig().setName(columnName).setType("varchar(20)"));
        }

        return new Change[] {createTableChange };
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) throws Exception {
        //todo
    }
}
