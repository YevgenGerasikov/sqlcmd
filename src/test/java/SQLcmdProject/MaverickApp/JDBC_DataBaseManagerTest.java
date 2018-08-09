package SQLcmdProject.MaverickApp;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class JDBC_DataBaseManagerTest {
    //Тестируем методы передачи SQL запросов
    private static JDBC_DataBaseManager manager;
    private static ArraylistDataSet testDataSetInstance = new ArraylistDataSet();
    private static Connection connection;
    private static Statement stmt;
    private static ResultSet queryResult;
    private static ResultSetMetaData rsmd;


    @BeforeAll
    static void initAll() throws SQLException {
        manager = new JDBC_DataBaseManager();
        manager.connectToDatabase();
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");

    }

    @AfterAll
    static void afterAll() {
        try {
            connection.close();
            System.out.println("Соединение закрыто");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void initEach() throws SQLException {
        stmt = connection.createStatement();
        testDataSetInstance.setTableName("users");
    }

    @AfterEach
    void afterEach() throws SQLException {
        if (stmt != null && !stmt.isClosed()) {
            stmt.close();
            System.out.println("Инструкция Statement закрыта.");
        } else if (queryResult != null && !queryResult.isClosed()) {
            queryResult.close();
            System.out.println("Инструкция ResultSet закрыта.");
        }
    }
    //TODO Mockito need


    @Test
    void connectToDatabaseTest() {
        try {
            boolean expected = (!manager.connection.isClosed());
            assertEquals(expected, true, "Expected true");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addBlankTableTest() {
        try {
            stmt.executeUpdate("DROP TABLE IF EXISTS public.users");
            manager.addTable(testDataSetInstance);
            queryResult = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_name = 'users'");
            String expected = "users";
            String result = "";
            while (queryResult.next()) {
                result = queryResult.getString("table_name");
            }
            assertEquals(expected, result, "table not created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addTableWithColumnsTest() {
        try {
            stmt.executeUpdate("DROP TABLE IF EXISTS public.users");
            testDataSetInstance.userKeysInput.add("name");
            testDataSetInstance.userKeysInput.add("password");
            testDataSetInstance.userValuesInput.add("VARCHAR(25)");
            testDataSetInstance.userValuesInput.add("VARCHAR(25)");
            manager.addTable(testDataSetInstance);
            queryResult = stmt.executeQuery("SELECT * FROM public.users");
            rsmd = queryResult.getMetaData();
            int columnsCount = rsmd.getColumnCount();
            String[] expected = {"id", "name", "password"};
            String[] result = new String[columnsCount];
            for (int i = 1; i <= columnsCount; i++) {
                result[i - 1] = rsmd.getColumnName(i);
            }
            assertArrayEquals(expected, result, "expect id, name, password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addDataToTableTest() {
        try {
            testDataSetInstance.setTableName("test");
            queryResult = stmt.executeQuery("SELECT COUNT(1) FROM public.test");
            int before = 0;
            if (queryResult.next()) {
                before = queryResult.getInt(1);
            }
            testDataSetInstance.userKeysInput.add("name");
            testDataSetInstance.userValuesInput.add("'Vasyl140'");
            testDataSetInstance.userKeysInput.add("surname");
            testDataSetInstance.userValuesInput.add("'Gupalo1140'");
            testDataSetInstance.userKeysInput.add("password");
            testDataSetInstance.userValuesInput.add("'12345678p@'");
            manager.addDataToTable(testDataSetInstance);
            queryResult = stmt.executeQuery("SELECT COUNT(1) FROM public.test");
            int after = 0;
            if (queryResult.next()) {
                after = queryResult.getInt(1);
            }
            int expected = 1;
            int result = after - before;
            assertEquals(expected, result, "expected plus 1 row");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getTablesListTest() {
        manager.getTablesList(testDataSetInstance);
        //Mockito
    }

    @Test
    void getTableDataTest() {
        testDataSetInstance.setTableName("test");
        manager.getTableData(testDataSetInstance);
        //Mockito
    }

    @Test
        //Mockito
    void updateDataTest() {
        try {
            testDataSetInstance.setTableName("test");
            testDataSetInstance.userSetInputForTableEdit.add("password");
            testDataSetInstance.userSetInputForTableEdit.add("testPASSWOD2");
            testDataSetInstance.userSetInputForTableEdit.add("name");
            testDataSetInstance.userSetInputForTableEdit.add("testNAME2");
            testDataSetInstance.userWhereInputForTableEdit.add("id");
            testDataSetInstance.userWhereInputForTableEdit.add("6");
            manager.updateData(testDataSetInstance);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteDataTest() {
        try {
            testDataSetInstance.setTableName("test");
            testDataSetInstance.userInputForDeleteInfo.add("password");
            testDataSetInstance.userInputForDeleteInfo.add("12345678p@");
            manager.deleteData(testDataSetInstance);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}