package SQLcmdProject.MaverickApp.model;

import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JDBC_DataBaseManagerTest {
    //Тестируем методы передачи SQL запросов
    private static JDBC_DataBaseManager manager;
    private static UserInputHandler testDataSetInstance = new UserInputHandler();
    private static Connection connection;
    private static Statement stmt;
    private static ResultSet queryResult;
    private static ResultSetMetaData rsmd;
    private List<String> userInputAsList = new ArrayList<>();


    @BeforeAll
    static void initAll() throws SQLException {
        manager = new JDBC_DataBaseManager();
        manager.connectToDatabase("postgres", "postgres", "postgres");
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        stmt = connection.createStatement();
    }

    @AfterAll
    static void afterAll() {
        try {
            connection.close();
            System.out.println("Соединение закрыто");
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
                //System.out.println("Инструкция Statement закрыта.");
            } else if (queryResult != null && !queryResult.isClosed()) {
                queryResult.close();
                //System.out.println("Инструкция ResultSet закрыта.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void initEach() throws SQLException {
        testDataSetInstance.setTableName("users");
    }

    //TODO Mockito need


    @Test
    void connectToDatabase() {
        try {
            boolean expected = (!manager.connection.isClosed());
            assertEquals(expected, true, "Expected true");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    void addBlankTableTest() {
//        try {
//            stmt.executeUpdate("DROP TABLE IF EXISTS public.users");
//            manager.addTable(userInputAsList);
//            queryResult = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_name = 'users'");
//            String expected = "users";
//            String result = "";
//            while (queryResult.next()) {
//                result = queryResult.getString("table_name");
//            }
//            assertEquals(expected, result, "table not created");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    void addTable() {
        try {
            stmt.executeUpdate("DROP TABLE IF EXISTS public.users");
            userInputAsList.add("name");
            userInputAsList.add("password");
            manager.addTable("users", userInputAsList);
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
    void addDataToTable() {
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
    void getTablesList() {
        manager.getTablesList();
        //Mockito
    }

    @Test
    void getTableData() {
        manager.getTableData("test");
        //Mockito
    }

    @Test
        //Mockito
    void updateData() {
        testDataSetInstance.setTableName("test");
        testDataSetInstance.userSetInputForTableEdit.add("password");
        testDataSetInstance.userSetInputForTableEdit.add("testPASSWOD2");
        testDataSetInstance.userSetInputForTableEdit.add("name");
        testDataSetInstance.userSetInputForTableEdit.add("testNAME2");
        testDataSetInstance.userWhereInputForTableEdit.add("id");
        testDataSetInstance.userWhereInputForTableEdit.add("6");
        manager.updateData(testDataSetInstance);
    }

    @Test
    void deleteSelectedData() {
        testDataSetInstance.setTableName("test");
        testDataSetInstance.userInputForDeleteInfo.add("password");
        testDataSetInstance.userInputForDeleteInfo.add("12345678p@");
        manager.deleteSelectedData(testDataSetInstance);
    }

    @Test
    void deleteAllData() {
        manager.deleteAllData("users");
    }

    @Test
    void deleteTable() {
        manager.deleteTable("users");
    }
}