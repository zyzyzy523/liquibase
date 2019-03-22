package liquibase.ext.ora.dropcheck;

import liquibase.ext.ora.testing.BaseTestCase;
import org.junit.Before;
import org.junit.Test;

public class DropCheckDBTest extends BaseTestCase {

    @Test
    public void placeholder() {

    }

//    private IDataSet loadedDataSet;
//    private final String TABLE_NAME = "USER_CONSTRAINTS";
//
//    protected IDatabaseConnection getConnection() throws Exception {
//        return new DatabaseConnection(connection);
//    }
//
//    protected IDataSet getDataSet() throws Exception {
//        loadedDataSet = new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream(
//                "liquibase/ext/ora/dropcheck/input.xml"));
//        return loadedDataSet;
//    }
//
//    @Before
//    public void setUp() throws Exception {
//        changeLogFile = "liquibase/ext/ora/dropcheck/changelog.test.xml";
//        connectToDB();
//        cleanDB();
//    }
//
//    @Test
//    public void testCompare() throws Exception {
//        if (connection == null) {
//            return;
//        }
//        QueryDataSet actualDataSet = new QueryDataSet(getConnection());
//
//        liquiBase.update((String) null);
//
//        actualDataSet.addTable(TABLE_NAME, "select * from " + TABLE_NAME + " where constraint_name='TOM_CHECK'");
//        loadedDataSet = getDataSet();
//
//        Assertion.assertEquals(loadedDataSet, actualDataSet);
//    }
}
