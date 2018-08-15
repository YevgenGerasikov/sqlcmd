package SQLcmdProject.MaverickApp.controller;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import SQLcmdProject.MaverickApp.view.View;

import java.util.Arrays;
import java.util.List;

public class Controller {
    private View view;
    private DataBaseManager manager;
    private String[] formatedUserInput;
    private List<String> userInputAsList;
    private String tableName;

    public Controller(View view, DataBaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public void run() {
        connect();
        while (true) {
            view.write("Введите команду (или help для получения списка доступных команд):");
            String command = view.read();
            formatedUserInput = command.split("\\s\\|\\s");
            userInputAsList = Arrays.asList(formatedUserInput);
            if (formatedUserInput.length > 2 && formatedUserInput[0].equals("create")) {
                tableName = userInputAsList.get(1);
                doCreate(userInputAsList);
            } else if (formatedUserInput[0].equals("tables")) {
                doTables();
            } else if (formatedUserInput.length == 2 && formatedUserInput[0].equals("clear")) {
                tableName = userInputAsList.get(1);
                doClear();
            } else if (formatedUserInput.length == 2 && formatedUserInput[0].equals("drop")) {
                tableName = userInputAsList.get(1);
                doDrop();
            } else if (formatedUserInput.length == 2 && formatedUserInput[0].equals("find")) {
                tableName = userInputAsList.get(1);
                doFind();
            } else if (formatedUserInput.length > 3 && formatedUserInput[0].equals("insert")) {
                tableName = userInputAsList.get(1);
                doInsert(userInputAsList);
            } else if (formatedUserInput.length > 3 && formatedUserInput[0].equals("update")) {
                tableName = userInputAsList.get(1);
                doUpdate(userInputAsList);
            } else if (formatedUserInput.length > 3 && formatedUserInput[0].equals("delete")) {
                tableName = userInputAsList.get(1);
                doDelete(userInputAsList);
            } else if (formatedUserInput.length == 1 && formatedUserInput[0].equals("help")) {
                doHelp();
            } else if (formatedUserInput.length == 1 && formatedUserInput[0].equals("exit")) {
                view.write("До скорой встречи!");
                System.exit(0);
            } else {
                view.write("Несуществующая команда: " + userInputAsList);
            }
        }
    }


    private void connect() {
        view.write("Hello dude!");
        while (true) {
            try {
                view.write("Для подключения к соответствующей БД введите команду: connect | database | username | password");
                String userInput = view.read();
                //String userInput = "connect | database | username | password";
                String[] formatedUserInput = userInput.split("\\s\\|\\s");
                checkUserInputLenght(formatedUserInput, 4);
                String databaseURL = formatedUserInput[1];
                String username = formatedUserInput[2];
                String password = formatedUserInput[3];
                manager.connectToDatabase(databaseURL, username, password);
                break;
            } catch (Exception e) {
                printError(e);
            }
        }
    }

    private void checkUserInputLenght(String[] formatedUserInput, int lenght) {
        if (formatedUserInput.length != lenght) {
            throw new IllegalArgumentException("Неверно количество параметров разделенных знаком '|', ожидается " + lenght + ", но есть: " + formatedUserInput.length);
        }
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        Throwable cause = e.getCause();
        if (cause != null) {
            message += " " + cause.getMessage();
        }
        view.write("Неудача по причине: " + message);
        view.write("Повторите попытку.");
    }

    private void doCreate(List<String> userInputAsList) {
        userInputAsList = userInputAsList.subList(2, userInputAsList.size());
        manager.addTable(tableName, userInputAsList);
    }

    private void doTables() {
        manager.getTablesList();
    }

    private void doClear() {
        manager.deleteAllData(tableName);
    }

    private void doDrop() {
        manager.deleteTable(tableName);
    }

    private void doFind() {
        manager.getTableData(tableName);
    }

    private void doInsert(List<String> userInputAsList) {
        userInputAsList = userInputAsList.subList(2, userInputAsList.size());
        manager.addDataToTable(tableName, userInputAsList);
    }

    private void doUpdate(List<String> userInputAsList) {
        userInputAsList = userInputAsList.subList(2, userInputAsList.size());
        manager.updateData(tableName, userInputAsList);
    }

    private void doDelete(List<String> userInputAsList) {
        userInputAsList = userInputAsList.subList(2, userInputAsList.size());
        manager.deleteSelectedData(tableName, userInputAsList);
    }

    private void doHelp() {
        view.write("Существующие команды:");

        view.write("\ttables");
        view.write("\t\t•\tКоманда выводит список всех таблиц базы данных, к которой подключились");

        view.write("\tclear | tableName");
        view.write("\t\t•\tКоманда очищает все содержимое указанной 'tableName' таблицы");

        view.write("\tdrop | tableName");
        view.write("\t\t•\tКоманда удаляет заданную 'tableName' таблицу");

        view.write("\tcreate | tableName | columnName1 | columnNameN");
        view.write("\t\t•\tКоманда создает новую таблицу с именем 'tableName' и необходимых колонок с заданными именами 'columnName'");

        view.write("\tfind | tableName");
        view.write("\t\t•\tКоманда для получения содержимого (вывод на экран) указанной таблицы 'tableName'");

        view.write("\tinsert | tableName | columnName1 | columnName1Value | columnNameN | columnNameNValue");
        view.write("\t\t•\tКоманда для вставки одной строки в заданную таблицу 'tableName' где 'columnName1' - наименования столбца, " +
                "а 'columnName1Value' - желаемое значение");

        view.write("\tupdate | tableName | WhereColumnName | WhereColumnValue | SetColumnName | SetColumnValue");
        view.write("\t\t•\tКоманда вносит изменения со значение SetColumnValue в ячеки в столбце SetColumnName таблицы 'tableName' " +
                "где  'WhereColumnName' = 'WhereColumnValue'");

        view.write("\tdelete | tableName | ColumnName | ColumnValue");
        view.write("\t\t•\tКоманда удаляет одну или несколько записей для которых соблюдается условие ColumnName = ColumnValue");

        view.write("\thelp");
        view.write("\t\t•\tдля вывода этого списка на экран");

        view.write("\texit");
        view.write("\t\t•\tдля выхода из программы");
    }
}

