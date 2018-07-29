package SQLcmdProject.MaverickApp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArraylistDataSetTest {
    //тестируем методы формирования SQL запросов

    private  ArraylistDataSet testInstance = new ArraylistDataSet();
    private  String expectedEmpty = null;

    @Test
    void setTableNameTest() {
        testInstance.setTableName("users");
        String expected = "users";
        String result = testInstance.getTableName();
        assertEquals(expected, result, "users expected");
    }

    @Test
    void getTableNameTest() {
        String result = testInstance.getTableName();
        assertEquals(expectedEmpty, result, "null expected");
        testInstance.setTableName("newTable");
        String expected2 = "newTable";
        String result2 = testInstance.getTableName();
        assertEquals(expected2, result2, "newTable expected");
    }

    @Test
    void handleCreateUserInputTest() {
        testInstance.userKeysInput.add("name");
        testInstance.userKeysInput.add("password");
        testInstance.userValuesInput.add("VARCHAR(25)");
        testInstance.userValuesInput.add("VARCHAR(25)");
        String expected = "(id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))";
        String result = testInstance.handleCreateUserInput(testInstance.userKeysInput);
        assertEquals(expected, result, "expected (id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))");
    }

    @Test
    void handleAddUserInput() {
        testInstance.userKeysInput.add("name");
        testInstance.userValuesInput.add("'Vasyl140'");
        testInstance.userKeysInput.add("password");
        testInstance.userValuesInput.add("'12345678p'");
        String expectedKeys = "(name, password)";
        String expectedValues = "('Vasyl140', '12345678p')";
        String resultKeys = testInstance.handleAddUserInput(testInstance.userKeysInput);
        String resultValues = testInstance.handleAddUserInput(testInstance.userValuesInput);
        assertEquals(expectedKeys, resultKeys, "expected (name, password) keys");
        assertEquals(expectedValues, resultValues, "expected ('Vasyl140', '12345678p') values");
    }

    @Test
    void handleUpdateUserInput() {
        testInstance.userSetInputForTableEdit.add("password");
        testInstance.userSetInputForTableEdit.add("testPASSWOD2");
        testInstance.userSetInputForTableEdit.add("name");
        testInstance.userSetInputForTableEdit.add("testNAME2");
        testInstance.userWhereInputForTableEdit.add("name");
        testInstance.userWhereInputForTableEdit.add("Petro");
        String expectedSetValues = "password = 'testPASSWOD2', name = 'testNAME2'";
        String resultSetValues = testInstance.handleUpdateUserInput(testInstance.userSetInputForTableEdit);
        assertEquals(expectedSetValues, resultSetValues, "expected password = 'testPASSWOD2', name = 'testNAME2' keys");
        String expectedWhereValues = "name = 'Petro'";
        String resultWhereValues = testInstance.handleUpdateUserInput(testInstance.userWhereInputForTableEdit);
        assertEquals(expectedWhereValues, resultWhereValues, "expected name = 'Petro' values");
    }

    @Test
    void createTableQuery() {
        testInstance.setTableName("users");
        String expectedEmpty = "CREATE TABLE IF NOT EXISTS public.users()";
        String resultEmpty = testInstance.createTableQuery();
        assertEquals(expectedEmpty, resultEmpty, "expected = CREATE TABLE IF NOT EXISTS public.users()");
        testInstance.userKeysInput.add("name");
        testInstance.userKeysInput.add("password");
        testInstance.userValuesInput.add("VARCHAR(25)");
        testInstance.userValuesInput.add("VARCHAR(25)");
        String expected = "CREATE TABLE IF NOT EXISTS public.users(id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))";
        String result = testInstance.createTableQuery();
        assertEquals(expected, result, "expected = CREATE TABLE IF NOT EXISTS public.users(id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))");

    }

    @Test
    void addDataToTableQuery() {
        testInstance.setTableName("users");
        String expectedEmpty = "INSERT INTO public.users() VALUES()";
        String resultEmpty = testInstance.addDataToTableQuery();
        assertEquals(expectedEmpty, resultEmpty, "Expected = INSERT INTO public.users() VALUES()");
        testInstance.userKeysInput.add("name");
        testInstance.userValuesInput.add("'Vasyl140'");
        testInstance.userKeysInput.add("password");
        testInstance.userValuesInput.add("'12345678p'");
        String expected = "INSERT INTO public.users(name, password) VALUES('Vasyl140', '12345678p')";
        String result = testInstance.addDataToTableQuery();
        assertEquals(expected, result, "Expected = INSERT INTO public.users(name, password) VALUES('Vasyl140', '12345678p')");
    }

    @Test
    void getTablesListQuery() {
        String expected = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'";
        String result = testInstance.getTablesListQuery();
        assertEquals(expected, result, "Expected = SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'");
    }

    @Test
    void getTableDataQuery() {
        testInstance.setTableName("users");
        String expected = "SELECT * FROM public.users";
        String result = testInstance.getTableDataQuery();
        assertEquals(expected, result, "Expected = SELECT * FROM public.users");
    }

    @Test
    void updateDataInTableQuery() {
        testInstance.setTableName("users");
        String expectedEmpty = "UPDATE public.users SET  WHERE ";
        String resultEmpty = testInstance.updateDataInTableQuery();
        assertEquals(expectedEmpty, resultEmpty, "Expected = UPDATE public.users SET  WHERE ");
        testInstance.userSetInputForTableEdit.add("password");
        testInstance.userSetInputForTableEdit.add("testPASSWOD2");
        testInstance.userWhereInputForTableEdit.add("name");
        testInstance.userWhereInputForTableEdit.add("Petro");
        String expected = "UPDATE public.users SET password = 'testPASSWOD2' WHERE name = 'Petro'";
        String result = testInstance.updateDataInTableQuery();
        assertEquals(expected, result, "Expected = UPDATE public.users SET password = 'testPASSWOD2' WHERE name = 'Petro'");
    }

    @Test
    void deleteDataInTableQuery() {
        testInstance.setTableName("users");
        String expectedEmpty = "DELETE FROM public.users WHERE ";
        String resultEmpty = testInstance.deleteDataInTableQuery();
        assertEquals(expectedEmpty, resultEmpty);
        testInstance.userInputForDeleteInfo.add("name");
        testInstance.userInputForDeleteInfo.add("Petro24");
        String expected = "DELETE FROM public.users WHERE name = 'Petro24'";
        String result = testInstance.deleteDataInTableQuery();
        assertEquals(expected, result);
    }
}