package liquibase.ext.ora.adddeferredprimarykey;

import liquibase.Scope;
import liquibase.change.Change;
import liquibase.changelog.ChangeLog;
import liquibase.changelog.ChangeSet;
import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.ext.ora.testing.BaseTestCase;
import liquibase.parser.ParserFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.statement.SqlStatement;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AddDeferredPrimaryKeyChangeTest extends BaseTestCase {

    @Before
    public void setUp() throws Exception {
        changeLogFile = "liquibase/ext/ora/adddeferredprimarykey/changelog.test.xml";
        connectToDB();
        cleanDB();
    }

//    @Test
//    public void getChangeMetaData() {
//        AddDeferredPrimaryKeyChange addPrimaryKeyChange = new AddDeferredPrimaryKeyChange();
//        assertEquals("addDeferredPrimaryKey", Scope.getCurrentScope().getSingleton(ChangeFactory.class).getChangeMetaData(addPrimaryKeyChange).getName());
//        assertEquals("Add deferred primary key", Scope.getCurrentScope().getSingleton(ChangeFactory.class).getChangeMetaData(addPrimaryKeyChange).getDescription());
//        assertEquals(ChangeMetaData.PRIORITY_DEFAULT, Scope.getCurrentScope().getSingleton(ChangeFactory.class).getChangeMetaData(addPrimaryKeyChange).getPriority());
//    }

    @Test
    public void getConfirmationMessage() throws Exception {
        AddDeferredPrimaryKeyChange change = new AddDeferredPrimaryKeyChange();
        change.setTableName("TABLE_NAME");
        change.setColumnNames("COL_HERE");

        assertEquals("Deferred primary key added to TABLE_NAME (COL_HERE)", change.getConfirmationMessage());
    }

    @Test
    public void generateStatement() throws Exception {

        AddDeferredPrimaryKeyChange change = new AddDeferredPrimaryKeyChange();
        change.setSchemaName("SCHEMA_NAME");
        change.setTableName("TABLE_NAME");
        change.setColumnNames("COL_HERE");
        change.setConstraintName("PK_NAME");

        change.setDeferrable(true);
        change.setInitiallyDeferred(true);

        Database database = new OracleDatabase();
        SqlStatement[] sqlStatements = change.generateStatements(database);

        assertEquals(1, sqlStatements.length);
        assertTrue(sqlStatements[0] instanceof AddDeferredPrimaryKeyStatement);

        assertEquals("SCHEMA_NAME", ((AddDeferredPrimaryKeyStatement) sqlStatements[0]).getSchemaName());
        assertEquals("TABLE_NAME", ((AddDeferredPrimaryKeyStatement) sqlStatements[0]).getTableName());
        assertEquals("COL_HERE", ((AddDeferredPrimaryKeyStatement) sqlStatements[0]).getColumnNames());
        assertEquals("PK_NAME", ((AddDeferredPrimaryKeyStatement) sqlStatements[0]).getConstraintName());

        assertTrue(((AddDeferredPrimaryKeyStatement) sqlStatements[0]).getDeferrable());
        assertTrue(((AddDeferredPrimaryKeyStatement) sqlStatements[0]).getInitiallyDeferred());
    }

    @Test
    public void parseAndGenerate() throws Exception {
        if (connection == null) {
            return;
        }

        Database database = liquiBase.getDatabase();
        ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor();

        ChangeLog changeLog = Scope.getCurrentScope().getSingleton(ParserFactory.class).parse(null, changeLogFile, ChangeLog.class);;

        changeLog.validate(database);

        List<ChangeSet> changeSets = changeLog.getChangeSets();

        List<String> expectedQuery = new ArrayList<String>();

        expectedQuery.add("CREATE TABLE LBUSER.AddDeferredPrimaryKeyTest (id NUMBER(10), name VARCHAR2(50))");
        expectedQuery.add("ALTER TABLE LBUSER.AddDeferredPrimaryKeyTest ADD CONSTRAINT PK_AddDeferredPrimaryKeyTest PRIMARY KEY (id) DEFERRABLE INITIALLY DEFERRED");

        int i = 0;

        for (ChangeSet changeSet : changeSets) {
            for (Change change : changeSet.getChanges()) {
                Sql[] sql = SqlGeneratorFactory.getInstance().generateSql(change.generateStatements(database)[0], database);
                assertEquals(expectedQuery.get(i), sql[0].toSql());
            }
            i++;
        }
    }

    @Test
    public void test() throws Exception {
        if (connection == null) {
            return;
        }

        liquiBase.update((String) null);

    }
}
