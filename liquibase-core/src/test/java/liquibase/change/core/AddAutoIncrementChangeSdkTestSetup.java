package liquibase.change.core;

import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.diff.DiffResult;
import liquibase.diff.ObjectDifferences;
import liquibase.exception.DatabaseException;
import liquibase.sdk.standardtests.change.StandardChangeSdkTestSetup;
import liquibase.structure.core.Column;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AddAutoIncrementChangeSdkTestSetup extends StandardChangeSdkTestSetup {
    @Override
    protected Change[]  prepareDatabase() throws DatabaseException {
        AddAutoIncrementChange change = (AddAutoIncrementChange) getChange();

        CreateTableChange createTable = new CreateTableChange();
        createTable.setCatalogName(change.getCatalogName());
        createTable.setSchemaName(change.getSchemaName());
        createTable.setTableName(change.getTableName());
        String dataType = change.getColumnDataType();
        if (dataType == null) {
            dataType = "int";
        }
        createTable.addColumn(new ColumnConfig().setName(change.getColumnName()).setType(dataType).setConstraints(new ConstraintsConfig().setPrimaryKey(true).setNullable(false)));

        return new Change[] {createTable};
    }

    @Override
    protected void checkDiffResult(DiffResult diffResult) {
        AddAutoIncrementChange change = (AddAutoIncrementChange) getChange();
        Column example = new Column().setName(change.getColumnName()).setRelation(new Table().setName(change.getTableName()).setSchema(new Schema(change.getCatalogName(), change.getSchemaName())));
        ObjectDifferences changes = diffResult.getChangedObject(example, getDatabase());
        assertNotNull(changes);
        assertNull(changes.getDifference("autoIncrementInformation").getReferenceValue());
        assertNotNull(changes.getDifference("autoIncrementInformation").getComparedValue());

        assertNull(changes.getDifference("dataType"));
    }
}
