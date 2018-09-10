package SQLcmdProject.MaverickApp.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserInputHandlerTest {
    //тестируем методы формирования SQL запросов

    private UserInputHandler testHandlerInstance = new UserInputHandler();
    private List<String> userInputAsList = new ArrayList<>();


    @Test
    void handleCreateUserInput() {
        userInputAsList.add("name");
        userInputAsList.add("password");
        String expected = "(id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))";
        String result = testHandlerInstance.handleCreateUserInput(userInputAsList);
        assertEquals(expected, result, "expected (id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))");
    }

    @Test
    void handleInsertUserInputKeys() {
        userInputAsList.add("name");
        userInputAsList.add("Vasyl140");
        String expected = "(name, Vasyl140)";
        String result = testHandlerInstance.handleInsertUserInputKeys(userInputAsList);
        assertEquals(expected, result, "expected: (name, Vasyl140)");
    }

    @Test
    void handleInsertUserInputValues() {
        userInputAsList.add("name");
        userInputAsList.add("Vasyl140");
        String expected = "('name', 'Vasyl140')";
        String result = testHandlerInstance.handleInsertUserInputValues(userInputAsList);
        assertEquals(expected, result, "expected: (name, Vasyl140)");
    }

    @Test
    void handleUpdateUserInput() {
        userInputAsList.add("password");
        userInputAsList.add("testPASSWOD2");
        userInputAsList.add("name");
        userInputAsList.add("testNAME2");
        String expectedSetValues = "password = 'testPASSWOD2', name = 'testNAME2'";
        String resultSetValues = testHandlerInstance.handleUpdateUserInput(userInputAsList);
        assertEquals(expectedSetValues, resultSetValues, "expected password = 'testPASSWOD2', name = 'testNAME2' keys");
    }

    @Test
    void createTableQuery() {
        String expectedEmpty = "CREATE TABLE IF NOT EXISTS public.users()";
        String resultEmpty = testHandlerInstance.createTableQuery("users", userInputAsList);
        assertEquals(expectedEmpty, resultEmpty, "expected = CREATE TABLE IF NOT EXISTS public.users()");
        userInputAsList.add("name");
        userInputAsList.add("password");
        String expected = "CREATE TABLE IF NOT EXISTS public.users(id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))";
        String result = testHandlerInstance.createTableQuery("users", userInputAsList);
        assertEquals(expected, result, "expected = CREATE TABLE IF NOT EXISTS public.users(id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))");

    }

    @Test
    void addDataToTableQuery() {
        userInputAsList.add("name");
        userInputAsList.add("Vasyl140");
        userInputAsList.add("password");
        userInputAsList.add("12345678p");
        String expected = "INSERT INTO public.users(name, password) VALUES('Vasyl140', '12345678p')";
        String result = testHandlerInstance.addDataToTableQuery("users", userInputAsList);
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
        userInputAsList.add("id");
        userInputAsList.add("6");
        userInputAsList.add("name");
        userInputAsList.add("TEST");
        userInputAsList.add("password");
        userInputAsList.add("TESTPASSWORD");
        String expected = "UPDATE public.test SET name = 'TEST', password = 'TESTPASSWORD' WHERE id = '6'";
        String result = testHandlerInstance.updateDataInTableQuery("test", userInputAsList);
        assertEquals(expected, result, "Expected = UPDATE public.users SET password = 'testPASSWOD2' WHERE name = 'Petro'");
    }

    @Test
    void deleteDataInTableQuery() {
        userInputAsList.add("name");
        userInputAsList.add("Petro24");
        String expected = "DELETE FROM public.users WHERE name = 'Petro24'";
        String result = testHandlerInstance.deleteDataInTableQuery("users", userInputAsList);
        assertEquals(expected, result);
    }

    @Test
    void deleteAllDataInTableQuery() {
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