package SQLcmdProject.MaverickApp.controller;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import SQLcmdProject.MaverickApp.view.View;

import java.util.Arrays;
import java.util.List;

public class Controller {
    private View view;
    private DataBaseManager manager;
    private String [] formatedUserInput;
    private List<String> userInputAsList;
    private String tableName;

    public Controller(View view, DataBaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public void run() {
        connect();
        while (true) {
            view.write("Введи команду (или help для помощи):");
            String command = view.read();
            String [] formatedUserInput = command.split("\\s\\|\\s");
            userInputAsList = Arrays.asList(formatedUserInput);
            if (formatedUserInput.length > 2 && formatedUserInput[0].equals("create")) {
                tableName = userInputAsList.get(1);
                doCreate(userInputAsList);
            }
            else if (formatedUserInput[0].equals("tables")) {
                doTables();
            }
            else if (formatedUserInput.length == 2 && formatedUserInput[0].equals("clear")) {
                tableName = userInputAsList.get(1);
                doClear();
            }
            else if (formatedUserInput.length == 2 && formatedUserInput[0].equals("drop")) {
                tableName = userInputAsList.get(1);
                doDrop();
            }
            else if (formatedUserInput.length == 2 && formatedUserInput[0].equals("find")) {
                tableName = userInputAsList.get(1);
                doFind();
            }
//            else if (command[0].equals("list")) {
//                doList();
//            }
//            else if (command.equals("help")) {
//                doHelp();
//            }
//            else if (command.equals("exit")) {
//                view.write("До скорой встречи!");
//                System.exit(0);
//            }
//            else {
//                view.write("Несуществующая команда: " + command);
//            }
        }
    }

    private void connect() {
        view.write("Hello dude!");
        while (true) {
            try {
                view.write("Для подключения к соответствующей БД введите команду: connect | database | username | password");
                String userInput = view.read();
                //String userInput = "connect | database | username | password";
                String [] formatedUserInput = userInput.split("\\s\\|\\s");
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
            throw new IllegalArgumentException("Неверно количество параметров разделенных знаком '|', ожидается " + lenght+ ", но есть: " + formatedUserInput.length);
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
        userInputAsList = userInputAsList.subList(2,userInputAsList.size());
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
}
