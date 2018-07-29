package SQLcmdProject.MaverickApp;

import java.util.List;
import java.util.ArrayList;

public class ArraylistDataSet {
    //Controller (MVC) part
    // реализует методы для обработки уже полученого пользовательского ввода
    // и формирования PostresSQL команды для передачи в вызывающий метод

    //TODO сделать литсы private. для для этого реализовать в их сеттерах обработку данных из IO интерфейсов
    public List<String> userKeysInput = new ArrayList<>();
    public List<String> userValuesInput = new ArrayList<>();
    public List<String> userSetInputForTableEdit = new ArrayList<>();
    public List<String> userWhereInputForTableEdit = new ArrayList<>();
    public List<String> userInputForDeleteInfo = new ArrayList<>();
    private String tableName;

    public static void main(String[] args) {


    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String handleCreateUserInput(List<String> userKeysInput) {
        String userInputString = "";
        if (userKeysInput.isEmpty()) {
            userInputString = "()";
        } else {
            //TODO решить вопрос с генерацией поля "id"
            userInputString += "(id serial PRIMARY KEY, ";
            for (int i = 0; i < userKeysInput.size() - 1; i++) {
                String value = userKeysInput.get(i) + " VARCHAR(25)";
                userInputString += value + ", ";
            }
            userInputString += userKeysInput.get(userKeysInput.size() - 1) + " VARCHAR(25))";
        }
        return userInputString;
    }

    public String handleAddUserInput(List<String> userInput) {
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

    String handleUpdateUserInput(List<String> userInput) {
        String userInputString = "";

        if (userInput.isEmpty()) {
            System.out.println("Не введены все необходимые значения для этой команды. Повторите ввод.");
        } else {
            //UPDATE public.users SET password = ? WHERE id = 3"
            userInputString += userInput.get(0) + " = '" + userInput.get(1) + "'";
            for (int i = 2; i < userInput.size(); i += 2) {
                userInputString += ", " + userInput.get(i) + " = '" + userInput.get(i + 1) + "'";
            }
        }
        return userInputString;
    }

    public String createTableQuery() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS public." + getTableName();
        createTableQuery += handleCreateUserInput(userKeysInput);
        return createTableQuery;
    }

    public String addDataToTableQuery() {
        //insertDataQuery = "INSERT INTO public.users(name, password) VALUES('papa', '123456')";
        String addDataToTableQuery = "INSERT INTO public." + getTableName();
        addDataToTableQuery += handleAddUserInput(userKeysInput);
        addDataToTableQuery += " VALUES" + handleAddUserInput(userValuesInput);
        System.out.println("Запрос для вставки нового значения в таблицу: " + addDataToTableQuery);
        return addDataToTableQuery;
    }

    public String getTablesListQuery() {
        return "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'";
    }

    public String getTableDataQuery() {
        return "SELECT * FROM public." + getTableName();
    }

    public String updateDataInTableQuery() {
        //UPDATE public.users SET password = ? WHERE id = 3"
        String addDataToTableQuery = "UPDATE public." + getTableName() + " SET ";
        addDataToTableQuery += handleUpdateUserInput(userSetInputForTableEdit);
        addDataToTableQuery += " WHERE " + handleUpdateUserInput(userWhereInputForTableEdit);
        //System.out.println("Запрос для изменения значения в таблице: " + addDataToTableQuery);
        return addDataToTableQuery;
    }


    public String deleteDataInTableQuery() {
        //"DELETE FROM public.users WHERE id > 4"
        String deleteDataFromTableQuery = "DELETE FROM public." + getTableName() + " WHERE ";
        deleteDataFromTableQuery += handleUpdateUserInput(userInputForDeleteInfo);
        return deleteDataFromTableQuery;
    }
}
