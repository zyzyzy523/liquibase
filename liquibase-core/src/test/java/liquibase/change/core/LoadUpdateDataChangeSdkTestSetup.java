package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.diff.DiffResult;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class LoadUpdateDataChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws Exception {
        LoadUpdateDataChange change = (LoadUpdateDataChange) getChange();

        InsertDataChange insertDataChange1 = new InsertDataChange();
        insertDataChange1.setCatalogName(change.getCatalogName());
        insertDataChange1.setSchemaName(change.getSchemaName());
        insertDataChange1.setTableName(change.getTableName());

        InsertDataChange insertDataChange2 = new InsertDataChange();
        insertDataChange2.setCatalogName(change.getCatalogName());
        insertDataChange2.setSchemaName(change.getSchemaName());
        insertDataChange2.setTableName(change.getTableName());

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        for (ColumnConfig column : change.getColumns()) {
            createTableChange.addColumn(column);
            insertDataChange1.addColumn(new ColumnConfig().setName(change.getPrimaryKey()));
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
