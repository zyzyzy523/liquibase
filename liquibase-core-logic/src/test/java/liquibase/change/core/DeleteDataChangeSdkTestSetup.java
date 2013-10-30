package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;

public class DeleteDataChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    public String setup() throws DatabaseException {
        DeleteDataChange change = (DeleteDataChange) getChange();

        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setCatalogName(change.getCatalogName());
        createTableChange.setSchemaName(change.getSchemaName());
        createTableChange.setTableName(change.getTableName());
        createTableChange.addColumn(new ColumnConfig().setName("id").setType("int"));
        createTableChange.addColumn(new ColumnConfig().setName("name").setType("varchar(255)"));

        InsertDataChange insertDataChange1 = new InsertDataChange();
        insertDataChange1.setCatalogName(change.getCatalogName());
        insertDataChange1.setSchemaName(change.getSchemaName());
        insertDataChange1.setTableName(change.getTableName());
        insertDataChange1.addColumn(new ColumnConfig().setName("id").setType("int").setValueNumeric(1));
        insertDataChange1.addColumn(new ColumnConfig().setName("name").setType("varchar(255)").setValue("Row A"));

        InsertDataChange insertDataChange2 = new InsertDataChange();
        insertDataChange2.setCatalogName(change.getCatalogName());
        insertDataChange2.setSchemaName(change.getSchemaName());
        insertDataChange2.setTableName(change.getTableName());
        insertDataChange2.addColumn(new ColumnConfig().setName("id").setType("int").setValueNumeric(1));
        insertDataChange2.addColumn(new ColumnConfig().setName("name").setType("varchar(255)").setValue("Row A"));

        execute(createTableChange, insertDataChange1, insertDataChange2);

        return null;
    }
}
