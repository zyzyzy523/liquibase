package liquibase.ext.ora.longupdate;

import liquibase.ext.ora.testing.BaseTestCase;
import org.junit.Before;
import org.junit.Test;

public class LongUpdateDBTest extends BaseTestCase {

    @Test
    public void placeholder() {

    }

//    protected IDatabaseConnection getConnection() throws Exception {
//        return new DatabaseConnection(connection);
//    }
//
//    protected IDataSet getDataSet() throws Exception {
//        loadedDataSet = new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream(
//                "liquibase/ext/ora/longupdate/input.xml"));
//        return loadedDataSet;
//    }
//
//    private IDataSet loadedDataSet;
//
//    @Before
//    public void setUp() throws Exception {
//        changeLogFile = "liquibase/ext/ora/longupdate/changelog.test.xml";
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
//        actualDataSet.addTable("LONGUPDATETEST", "SELECT * FROM LONGUPDATETEST");
//        loadedDataSet = getDataSet();
//
//        Assertion.assertEquals(loadedDataSet, actualDataSet);
//    }
}
