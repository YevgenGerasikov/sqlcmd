package SQLcmdProject.MaverickApp.model;

import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JDBC_DataBaseManagerTest {
    //Тестируем методы передачи SQL запросов
    private static JDBC_DataBaseManager manager;
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
            queryResult = stmt.executeQuery("SELECT COUNT(1) FROM public.test");
            int before = 0;
            if (queryResult.next()) {
                before = queryResult.getInt(1);
            }
            userInputAsList.add("name");
            userInputAsList.add("Vasyl140");
            userInputAsList.add("surname");
            userInputAsList.add("Gupalo1140");
            userInputAsList.add("password");
            userInputAsList.add("12345678p@");
            manager.addDataToTable("test", userInputAsList);
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
        userInputAsList.add("id");
        userInputAsList.add("6");
        userInputAsList.add("password");
        Integer randomPass = (int) (Math.random() * 1000 + 17);
        userInputAsList.add("TEST" + randomPass.toString());
        userInputAsList.add("name");
        userInputAsList.add("testNAME");
        manager.updateData("test", userInputAsList);
    }

    @Test
    void deleteSelectedData() {
        userInputAsList.add("password");
        userInputAsList.add("12345678p@");
        manager.deleteSelectedData("test", userInputAsList);
    }

    @Test
    void deleteAllData() {
        manager.deleteAllData("users");
    }

    @Test
    void deleteTable() {
        manager.deleteTable("users");
    }

    @Test
    void isConnected() {
        assertTrue(manager.isConnected());
    }
}