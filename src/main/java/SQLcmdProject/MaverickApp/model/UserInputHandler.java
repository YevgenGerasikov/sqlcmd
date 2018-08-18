package SQLcmdProject.MaverickApp.model;

import java.util.List;
import java.util.ArrayList;

public class UserInputHandler {

    public String handleCreateUserInput(List<String> userKeysInput) {
        String userInputString = "";
        if (userKeysInput.isEmpty()) {
            userInputString = "()";
        } else {
            userInputString += "(id serial PRIMARY KEY, ";
            for (int i = 0; i < userKeysInput.size() - 1; i++) {
                String value = userKeysInput.get(i) + " VARCHAR(25)";
                userInputString += value + ", ";
            }
            userInputString += userKeysInput.get(userKeysInput.size() - 1) + " VARCHAR(25))";
        }
        return userInputString;
    }

    public String handleInsertUserInputKeys(List<String> userInput) {
        String userInputString = "";
        if (userInput.isEmpty()) {
            userInputString = "()";
        } else {
            userInputString += "(";
            for (int i = 0; i < userInput.size() - 1; i++) {
                String value = userInput.get(i);
                userInputString += value + ", ";
            }
            userInputString += userInput.get(userInput.size() - 1) + ")";
        }
        return userInputString;
    }

    public String handleInsertUserInputValues(List<String> userInput) {
        String userInputString = "";
        if (userInput.isEmpty()) {
            userInputString = "()";
        } else {
            userInputString += "(";
            for (int i = 0; i < userInput.size() - 1; i++) {
                String value = userInput.get(i);
                userInputString += "'" + value + "', ";
            }
            userInputString += "'" + userInput.get(userInput.size() - 1) + "')";
        }
        return userInputString;
    }

    String handleUpdateUserInput(List<String> userInput) {
        String userInputString = "";
        //UPDATE public.users SET password = ? WHERE id = 3"
        userInputString += userInput.get(0) + " = '" + userInput.get(1) + "'";
        for (int i = 2; i < userInput.size(); i += 2) {
            userInputString += ", " + userInput.get(i) + " = '" + userInput.get(i + 1) + "'";
        }
        return userInputString;
    }

    public String createTableQuery(String tableName, List<String> userInputAsList) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS public." + tableName;
        createTableQuery += handleCreateUserInput(userInputAsList);
        return createTableQuery;
    }

    public String addDataToTableQuery(String tableName, List<String> userInputAsList) {
        //insertDataQuery = "INSERT INTO public.users(name, password) VALUES('papa', '123456')";
        List<String> userKeysInput = new ArrayList<>();
        List<String> userValuesInput = new ArrayList<>();
        for (int i = 0; i < userInputAsList.size(); i++) {
            if (i % 2 == 0) {
                userKeysInput.add(userInputAsList.get(i));
            } else {
                userValuesInput.add(userInputAsList.get(i));
            }
        }
        String addDataToTableQuery = "INSERT INTO public." + tableName;
        addDataToTableQuery += handleInsertUserInputKeys(userKeysInput);
        addDataToTableQuery += " VALUES" + handleInsertUserInputValues(userValuesInput);
        //System.out.println("Запрос для вставки нового значения в таблицу: " + addDataToTableQuery);
        return addDataToTableQuery;
    }

    public String getTablesListQuery() {
        return "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'";
    }

    public String getTableDataQuery(String tableName) {
        return "SELECT * FROM public." + tableName;
    }

    public String updateDataInTableQuery(String tableName, List<String> userInputAsList) {
        //UPDATE public.users SET password = ? WHERE id = 3"
        List<String> userWhereInput = userInputAsList.subList(0, 2);
        List<String> userSetInput = userInputAsList.subList(2, userInputAsList.size());
        String addDataToTableQuery = "UPDATE public." + tableName + " SET ";
        addDataToTableQuery += handleUpdateUserInput(userSetInput);
        addDataToTableQuery += " WHERE " + handleUpdateUserInput(userWhereInput);
        //System.out.println("Запрос для изменения значения в таблице: " + addDataToTableQuery);
        return addDataToTableQuery;
    }


    public String deleteDataInTableQuery(String tableName, List<String> userInputAsList) {
        //"DELETE FROM public.users WHERE id > 4"
        String deleteDataFromTableQuery = "DELETE FROM public." + tableName + " WHERE ";
        deleteDataFromTableQuery += handleUpdateUserInput(userInputAsList);
        return deleteDataFromTableQuery;
    }

    public String deleteAllDataInTableQuery(String tableName) {
        return "DELETE FROM public." + tableName;
    }

    public String deleteTableQuery(String tableName) {
        return "DROP TABLE " + tableName;
    }

}
