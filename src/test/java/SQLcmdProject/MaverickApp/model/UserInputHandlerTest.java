package SQLcmdProject.MaverickApp.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserInputHandlerTest {
    //тестируем методы формирования SQL запросов

    private UserInputHandler testHandlerInstance = new UserInputHandler();
    private List<String> userInputAsList = new ArrayList<>();
    private  String expectedEmpty = null;

    @Test
    void setTableNameTest() {
        testHandlerInstance.setTableName("users");
        String expected = "users";
        String result = testHandlerInstance.getTableName();
        assertEquals(expected, result, "users expected");
    }

    @Test
    void getTableNameTest() {
        String result = testHandlerInstance.getTableName();
        assertEquals(expectedEmpty, result, "null expected");
        testHandlerInstance.setTableName("newTable");
        String expected2 = "newTable";
        String result2 = testHandlerInstance.getTableName();
        assertEquals(expected2, result2, "newTable expected");
    }

    @Test
    void handleCreateUserInput() {
        userInputAsList.add("name");
        userInputAsList.add("password");
        String expected = "(id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))";
        String result = testHandlerInstance.handleCreateUserInput(userInputAsList);
        assertEquals(expected, result, "expected (id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))");
    }

    @Test
    void handleAddUserInput() {
        testHandlerInstance.userKeysInput.add("name");
        testHandlerInstance.userValuesInput.add("'Vasyl140'");
        testHandlerInstance.userKeysInput.add("password");
        testHandlerInstance.userValuesInput.add("'12345678p'");
        String expectedKeys = "(name, password)";
        String expectedValues = "('Vasyl140', '12345678p')";
        String resultKeys = testHandlerInstance.handleAddUserInput(testHandlerInstance.userKeysInput);
        String resultValues = testHandlerInstance.handleAddUserInput(testHandlerInstance.userValuesInput);
        assertEquals(expectedKeys, resultKeys, "expected (name, password) keys");
        assertEquals(expectedValues, resultValues, "expected ('Vasyl140', '12345678p') values");
    }

    @Test
    void handleUpdateUserInput() {
        testHandlerInstance.userSetInputForTableEdit.add("password");
        testHandlerInstance.userSetInputForTableEdit.add("testPASSWOD2");
        testHandlerInstance.userSetInputForTableEdit.add("name");
        testHandlerInstance.userSetInputForTableEdit.add("testNAME2");
        testHandlerInstance.userWhereInputForTableEdit.add("name");
        testHandlerInstance.userWhereInputForTableEdit.add("Petro");
        String expectedSetValues = "password = 'testPASSWOD2', name = 'testNAME2'";
        String resultSetValues = testHandlerInstance.handleUpdateUserInput(testHandlerInstance.userSetInputForTableEdit);
        assertEquals(expectedSetValues, resultSetValues, "expected password = 'testPASSWOD2', name = 'testNAME2' keys");
        String expectedWhereValues = "name = 'Petro'";
        String resultWhereValues = testHandlerInstance.handleUpdateUserInput(testHandlerInstance.userWhereInputForTableEdit);
        assertEquals(expectedWhereValues, resultWhereValues, "expected name = 'Petro' values");
    }

    @Test
    void createTableQuery() {
        testHandlerInstance.setTableName("users");
        String expectedEmpty = "CREATE TABLE IF NOT EXISTS public.users()";
        String resultEmpty = testHandlerInstance.createTableQuery(userInputAsList);
        assertEquals(expectedEmpty, resultEmpty, "expected = CREATE TABLE IF NOT EXISTS public.users()");
        testHandlerInstance.userKeysInput.add("name");
        testHandlerInstance.userKeysInput.add("password");
        testHandlerInstance.userValuesInput.add("VARCHAR(25)");
        testHandlerInstance.userValuesInput.add("VARCHAR(25)");
        String expected = "CREATE TABLE IF NOT EXISTS public.users(id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))";
        String result = testHandlerInstance.createTableQuery(userInputAsList);
        assertEquals(expected, result, "expected = CREATE TABLE IF NOT EXISTS public.users(id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))");

    }

    @Test
    void addDataToTableQuery() {
        testHandlerInstance.setTableName("users");
        String expectedEmpty = "INSERT INTO public.users() VALUES()";
        String resultEmpty = testHandlerInstance.addDataToTableQuery();
        assertEquals(expectedEmpty, resultEmpty, "Expected = INSERT INTO public.users() VALUES()");
        testHandlerInstance.userKeysInput.add("name");
        testHandlerInstance.userValuesInput.add("'Vasyl140'");
        testHandlerInstance.userKeysInput.add("password");
        testHandlerInstance.userValuesInput.add("'12345678p'");
        String expected = "INSERT INTO public.users(name, password) VALUES('Vasyl140', '12345678p')";
        String result = testHandlerInstance.addDataToTableQuery();
        assertEquals(expected, result, "Expected = INSERT INTO public.users(name, password) VALUES('Vasyl140', '12345678p')");
    }

    @Test
    void getTablesListQuery() {
        String expected = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'";
        String result = testHandlerInstance.getTablesListQuery();
        assertEquals(expected, result, "Expected = SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'");
    }

    @Test
    void getTableDataQuery() {
        String expected = "SELECT * FROM public.users";
        String result = testHandlerInstance.getTableDataQuery("users");
        assertEquals(expected, result, "Expected = SELECT * FROM public.users");
    }

    @Test
    void updateDataInTableQuery() {
        testHandlerInstance.setTableName("users");
        String expectedEmpty = "UPDATE public.users SET  WHERE ";
        String resultEmpty = testHandlerInstance.updateDataInTableQuery();
        assertEquals(expectedEmpty, resultEmpty, "Expected = UPDATE public.users SET  WHERE ");
        testHandlerInstance.userSetInputForTableEdit.add("password");
        testHandlerInstance.userSetInputForTableEdit.add("testPASSWOD2");
        testHandlerInstance.userWhereInputForTableEdit.add("name");
        testHandlerInstance.userWhereInputForTableEdit.add("Petro");
        String expected = "UPDATE public.users SET password = 'testPASSWOD2' WHERE name = 'Petro'";
        String result = testHandlerInstance.updateDataInTableQuery();
        assertEquals(expected, result, "Expected = UPDATE public.users SET password = 'testPASSWOD2' WHERE name = 'Petro'");
    }

    @Test
    void deleteDataInTableQuery() {
        testHandlerInstance.setTableName("users");
        String expectedEmpty = "DELETE FROM public.users WHERE ";
        String resultEmpty = testHandlerInstance.deleteDataInTableQuery();
        assertEquals(expectedEmpty, resultEmpty);
        testHandlerInstance.userInputForDeleteInfo.add("name");
        testHandlerInstance.userInputForDeleteInfo.add("Petro24");
        String expected = "DELETE FROM public.users WHERE name = 'Petro24'";
        String result = testHandlerInstance.deleteDataInTableQuery();
        assertEquals(expected, result);
    }
    @Test
    void  deleteAllDataInTableQuery(){
        String expected = "DELETE FROM public.users";
        String result = testHandlerInstance.deleteAllDataInTableQuery("users");
        assertEquals(expected, result);
    }

    @Test
    void deleteTableQuery() {
        String expected = "DROP TABLE users";
        String result = testHandlerInstance.deleteTableQuery("users");
        assertEquals(expected, result);
    }
}