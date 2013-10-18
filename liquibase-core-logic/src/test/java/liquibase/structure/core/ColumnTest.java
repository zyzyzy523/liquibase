package liquibase.structure.core;

import liquibase.change.ColumnConfig;
import liquibase.structure.DataType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ColumnTest {

    @Test
    public void toColumnConfig() {
        Table table = new Table();

        table.setPrimaryKey(new PrimaryKey().addColumnName(0, "colName").setName("pk_name").setTablespace("pk_tablespace"));
        table.getUniqueConstraints().add(new UniqueConstraint().setName("uq1").addColumn(0, "otherCol"));
        table.getUniqueConstraints().add(new UniqueConstraint().setName("uq2").addColumn(0, "colName"));

        table.getOutgoingForeignKeys().add(new ForeignKey().setName("fk1").setForeignKeyColumns("otherCol"));
        table.getOutgoingForeignKeys().add(new ForeignKey().setName("fk2").setForeignKeyColumns("colName").setPrimaryKeyTable(new Table().setName("otherTable")).setPrimaryKeyColumns("id"));

        Column column = new Column();
        column.setName("colName");
        column.setRelation(table);
        column.setAutoIncrementInformation(new Column.AutoIncrementInformation(3, 5));
        column.setType(new DataType("BIGINT"));
        column.setNullable(false);
        column.setDefaultValue(123);
        column.setRemarks("A Test Column");

        ColumnConfig config = column.toColumnConfig();

        assertEquals("colName", config.getName());
        assertEquals("123", config.getDefaultValue());
        assertEquals("A Test Column", config.getRemarks());
        assertEquals("BIGINT", config.getType());
        assertEquals(false, config.getConstraints().isNullable());

        assertEquals(true, config.getConstraints().isUnique());
        assertEquals("uq2", config.getConstraints().getUniqueConstraintName());

        assertEquals(true, config.getConstraints().isPrimaryKey());
        assertEquals("pk_name", config.getConstraints().getPrimaryKeyName());
        assertEquals("pk_tablespace", config.getConstraints().getPrimaryKeyTablespace());

        assertEquals("fk2", config.getConstraints().getForeignKeyName());
        assertEquals("otherTable(id)", config.getConstraints().getReferences());

        assertEquals(true, config.isAutoIncrement());
        assertEquals(3, config.getStartWith().longValue());
        assertEquals(5, config.getIncrementBy().longValue());
    }

    @Test
    public void constructor_nothingSet() {
        Table table = new Table();

        Column column = new Column();
        column.setName("colName");
        column.setRelation(table);
        column.setType(new DataType("BIGINT"));

        ColumnConfig config = column.toColumnConfig();

        assertEquals("colName", config.getName());
        assertNull(config.getDefaultValue());
        assertNull(config.getRemarks());
        assertEquals("BIGINT", config.getType());
        assertNull(config.getConstraints().isNullable()); //nullable could be unknown

        assertEquals(false, config.getConstraints().isUnique()); //we know it is unique or not, cannot return null
        assertNull(config.getConstraints().getUniqueConstraintName());

        assertEquals(false, config.getConstraints().isPrimaryKey()); //we know it is unique or not, cannot return null
        assertNull(config.getConstraints().getPrimaryKeyName());
        assertNull(config.getConstraints().getPrimaryKeyTablespace());

        assertNull(config.getConstraints().getForeignKeyName());
        assertNull(config.getConstraints().getReferences());

        assertEquals(false, config.isAutoIncrement());  //we know it is unique or not, cannot return null
        assertNull(config.getStartWith());
        assertNull(config.getIncrementBy());
    }

    @Test
    public void constructor_view() {
        View view = new View();

        Column column = new Column();
        column.setName("colName");
        column.setRelation(view);
        column.setType(new DataType("BIGINT"));

        ColumnConfig config = column.toColumnConfig();

        assertEquals("colName", config.getName());
        assertEquals("BIGINT", config.getType());

        assertNull(config.getConstraints()); //return null constraints for views

        assertNull(config.isAutoIncrement());  //set to null for views
    }

}
