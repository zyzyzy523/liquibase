package liquibase.datatype;

import liquibase.database.core.H2Database;
import liquibase.datatype.core.IntType;
import liquibase.datatype.core.IntTypeCore;
import liquibase.datatype.core.VarcharType;
import liquibase.datatype.core.VarcharTypeCore;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class DataTypeFactoryTest {
    @Test
    public void parse() throws Exception {
        assertParseCorrect("int", IntTypeCore.class);
        assertParseCorrect("varchar(255)", VarcharTypeCore.class);
        assertParseCorrect("int{autoIncrement:true}", "int", IntTypeCore.class);
        assertParseCorrect("int{}", "int", IntTypeCore.class);
        assertParseCorrect("varchar COLLATE Latin1_General_BIN", VarcharTypeCore.class);
        assertParseCorrect("varchar(255) COLLATE Latin1_General_BIN", VarcharTypeCore.class);

        assertTrue(((IntType) DataTypeFactory.getInstance().fromDescription("int{autoIncrement:true}")).isAutoIncrement());
        assertFalse(((IntType) DataTypeFactory.getInstance().fromDescription("int{autoIncrement:false}")).isAutoIncrement());
        assertFalse(((IntType) DataTypeFactory.getInstance().fromDescription("int")).isAutoIncrement());
        assertFalse(((IntType) DataTypeFactory.getInstance().fromDescription("int{}")).isAutoIncrement());
    }

    private void assertParseCorrect(String liquibaseString, String databaseString, Class<? extends LiquibaseDataType> expectedType) {
        LiquibaseDataType parsed = DataTypeFactory.getInstance().fromDescription(liquibaseString);
        assertEquals(expectedType.getName(), parsed.getClass().getName());
        assertEquals(databaseString, parsed.toString());
    }
    
    private void assertParseCorrect(String type, Class<? extends LiquibaseDataType> expectedType) {
        assertParseCorrect(type, type, expectedType);
    }
}
