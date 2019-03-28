package liquibase.ext.vertica;

import liquibase.Scope;
import liquibase.change.Change;
import liquibase.change.ChangeFactory;
import liquibase.change.ChangeMetaData;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.ChangeLog;
import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.ext.vertica.change.CreateProjectionChange;
import liquibase.ext.vertica.statement.CreateProjectionStatement;
import liquibase.parser.ParserFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.statement.SqlStatement;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CreateProjectionTest extends BaseTestCase {

    @Before
    public void setUp() throws Exception {
        changeLogFile = "C:\\Users\\vesterma\\Documents\\Projects\\liquibase\\target\\classes\\com.hp.db\\db_change2.xml";
        connectToDB();
        cleanDB();
    }

    @Ignore
    @Test
    public void getChangeMetaData() {
        CreateProjectionChange createMaterializedViewChange = new CreateProjectionChange();

        ChangeFactory changeFactory = Scope.getCurrentScope().getSingleton(ChangeFactory.class);
        assertEquals("createMaterializedView", changeFactory.getChangeMetaData(createMaterializedViewChange).getName());
        assertEquals("Create Materialized View", changeFactory.getChangeMetaData(createMaterializedViewChange).getDescription());
        assertEquals(ChangeMetaData.PRIORITY_DEFAULT, changeFactory.getChangeMetaData(createMaterializedViewChange).getPriority());
    }

    @Ignore
    @Test
    public void getConfirmationMessage() {
        CreateProjectionChange change = new CreateProjectionChange();
        change.setProjectionName("VIEW_NAME");

        assertEquals("Projection VIEW_NAME has been created", "Projection " + change.getProjectionName()
                + " has been created");
    }

    @Ignore
    @Test
    public void generateStatement() {

        CreateProjectionChange change = new CreateProjectionChange();
        change.setSchemaName("SCHEMA_NAME");
        change.setProjectionName("VIEW_NAME");
        change.setColumnAliases("COLUMN_ALIASES");
//        change.setObjectType("OBJECT_TYPE");
//        change.setTableSpace("TABLE_SPACE");
//        change.setQueryRewrite("QUERY_REWRITE");
        change.setSubquery("SUBQUERY");

//        change.setReducedPrecision(true);
//        change.setUsingIndex(true);
//        change.setForUpdate(true);

        Database database = new OracleDatabase();
        SqlStatement[] sqlStatements = change.generateStatements(database);

        assertEquals(1, sqlStatements.length);
        assertTrue(sqlStatements[0] instanceof CreateProjectionStatement);

        assertEquals("SCHEMA_NAME", ((CreateProjectionStatement) sqlStatements[0]).getSchemaName());
        assertEquals("VIEW_NAME", ((CreateProjectionStatement) sqlStatements[0]).getProjectionName());
//        assertEquals("COLUMN_ALIASES", ((CreateProjectionStatement) sqlStatements[0]).getColumnAliases());
//        assertEquals("OBJECT_TYPE", ((CreateProjectionStatement) sqlStatements[0]).getObjectType());
//        assertEquals("TABLE_SPACE", ((CreateProjectionStatement) sqlStatements[0]).getTableSpace());
//        assertEquals("QUERY_REWRITE", ((CreateProjectionStatement) sqlStatements[0]).getQueryRewrite());
        assertEquals("SUBQUERY", ((CreateProjectionStatement) sqlStatements[0]).getSubquery());

//        assertTrue(((CreateProjectionStatement) sqlStatements[0]).getReducedPrecision());
//        assertTrue(((CreateProjectionStatement) sqlStatements[0]).getUsingIndex());
//        assertTrue(((CreateProjectionStatement) sqlStatements[0]).getForUpdate());
    }

    @Ignore
    @Test
    public void parseAndGenerate() throws Exception {
        Database database = liquiBase.getDatabase();
        ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor();

        ChangeLog changeLog = Scope.getCurrentScope().getSingleton(ParserFactory.class).parse(null, changeLogFile, ChangeLog.class);

//        database.checkDatabaseChangeLogTable(false, changeLog, null);
        changeLog.validate(database);


        List<ChangeSet> changeSets = changeLog.getChangeSets();

        List<String> expectedQuery = new ArrayList<String>();

        expectedQuery.add("CREATE MATERIALIZED VIEW zuiolView ON PREBUILT TABLE AS select * from Table1");

        int i = 0;

        for (ChangeSet changeSet : changeSets) {
            for (Change change : changeSet.getChanges()) {
                Sql[] sql = SqlGeneratorFactory.getInstance().generateSql(change.generateStatements(database)[0], database);
                if (i == 4) {
                    assertEquals(expectedQuery.get(0), sql[0].toSql());
                }
            }
            i++;
        }
    }

    @Ignore
    @Test
    public void test() throws Exception {
//        liquiBase.update(null);
//        liquiBase.rollback(1, null);
    }

}
