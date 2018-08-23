package SQLcmdProject.MaverickApp.model;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

class JDBC_DataBaseManagerTest {
    //Тестируем методы передачи SQL запросов
    private static JDBC_DataBaseManager manager;
    private static Connection connection;
    private static ResultSet queryResult;
    private static ResultSetMetaData rsmd;
    private List<String> userInputAsList = new ArrayList<>();


    @BeforeAll
    static void initAll() {
        manager = new JDBC_DataBaseManager();
        manager.connectToDatabase("postgres", "postgres", "postgres");
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "postgres");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void checkConnections() {
        try {
            if (!connection.isClosed()) {
                connection.close();
                System.out.println("Соединение с БД закрыто.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
        try (Statement stmt = connection.createStatement()) {
            userInputAsList.add("name");
            userInputAsList.add("password");
            manager.addTable("tableForDrop", userInputAsList);
            queryResult = stmt.executeQuery("SELECT * FROM public.tableForDrop");
            rsmd = queryResult.getMetaData();
            int columnsCount = rsmd.getColumnCount();
            String[] expected = {"id", "name", "password"};
            String[] result = new String[columnsCount];
            for (int i = 1; i <= columnsCount; i++) {
                result[i - 1] = rsmd.getColumnName(i);
            }
            assertArrayEquals(expected, result, "expect id, name, password");
            stmt.executeUpdate("DROP TABLE IF EXISTS public.tableForDrop");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addDataToTable() {
        try (Statement stmt = connection.createStatement()) {
            int randomNumber = (int) (Math.random() * 1000 + 31);
            queryResult = stmt.executeQuery("SELECT COUNT(1) FROM public.users");
            int before = 0;
            if (queryResult.next()) {
                before = queryResult.getInt(1);
            }
            userInputAsList.add("name");
            userInputAsList.add("Name-" + randomNumber);
            userInputAsList.add("password");
            userInputAsList.add("Password-" + randomNumber);
            manager.addDataToTable("users", userInputAsList);
            queryResult = stmt.executeQuery("SELECT COUNT(1) FROM public.users");
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
        //Mockito test
        PrintStream out = Mockito.mock(PrintStream.class);
        System.setOut(out);
        manager.getTablesList();
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(out, atLeastOnce()).println(captor.capture());
        assertEquals("[Таблицы базы данных:, - tableforcleardata, " +
                "- statictable, - users]", captor.getAllValues().toString());
    }

    @Test
    void getTableData() {
        //Mockito test
        PrintStream out = Mockito.mock(PrintStream.class);
        System.setOut(out);
        manager.getTableData("staticTable");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(out, atLeastOnce()).println(captor.capture());
        Mockito.verify(out, atLeastOnce()).print(captor.capture());
        assertEquals("[--------------------------------, Данные из таблицы 'staticTable' выведены на экран, " +
                "name: StaticName , surname: StaticSurname ]", captor.getAllValues().toString());
    }

    @Test
    void printTableToConsole() {
        //Mockito test
        PrintStream out = Mockito.mock(PrintStream.class);
        System.setOut(out);
        manager.printTableToConsole("staticTable");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(out, atLeastOnce()).println(captor.capture());
        Mockito.verify(out, atLeastOnce()).print(captor.capture());
        assertEquals("[Печать 1 строки из таблицы statictable, |, " +
                        "Данные из таблицы 'staticTable' выведены на экран, " +
                        "+------------+---------------+\r\n" +
                        "|    name    |    surname    |\r\n" +
                        "+------------+---------------+\r\n" + ", " +
                        "| StaticName , | StaticSurname ]",
                captor.getAllValues().toString());
    }

    @Test
    void updateData() {
        try (Statement stmt = connection.createStatement()) {
            int randomNumber = (int) (Math.random() * 1000 + 31);
            userInputAsList.add("id");
            userInputAsList.add("1");
            userInputAsList.add("password");
            userInputAsList.add("newPassword-" + randomNumber);
            manager.updateData("users", userInputAsList);
            queryResult = stmt.executeQuery("SELECT password FROM public.users WHERE id = 1");
            String expected = "newPassword-" + randomNumber;
            String result = "";
            if (queryResult.next()) {
                result = queryResult.getString(1);
            }
            assertEquals(expected, result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteSelectedData() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("INSERT INTO public.tableforcleardata(name, password) VALUES('TestNameForDelete', '123456')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userInputAsList.add("name");
        userInputAsList.add("TestNameForDelete");

        PrintStream out = Mockito.mock(PrintStream.class);
        System.setOut(out);
        manager.deleteSelectedData("tableForClearData", userInputAsList);

        Mockito.verify(out).println("Удалено 1 строк");
    }

    @Test
    void deleteAllData() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS public.tableForClearData (id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))");
            stmt.executeUpdate("INSERT INTO public.tableForClearData(name, password) VALUES('TestNameForDelete', '123456')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        manager.deleteAllData("tableForClearData");
    }

    @Test
    void deleteTable() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS public.tableForDelete (id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        manager.deleteTable("tableForDelete");
    }

    @Test
    void isConnected() {
        assertTrue(manager.isConnected());
    }
}