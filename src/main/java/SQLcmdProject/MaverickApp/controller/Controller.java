package SQLcmdProject.MaverickApp.controller;

import SQLcmdProject.MaverickApp.controller.commands.*;
import SQLcmdProject.MaverickApp.model.DataBaseManager;
import SQLcmdProject.MaverickApp.view.ViewForSqlcmd;

import java.util.Arrays;
import java.util.List;

public class Controller {
    private ViewForSqlcmd view;
    private DataBaseManager manager;
    private String[] userInputAsArray;
    private List<String> userInputAsList;
    private String tableName;
    private Command[] commands;

    public Controller(ViewForSqlcmd view, DataBaseManager manager) {
        this.view = view;
        this.manager = manager;
        this.commands = new Command[]{
                new Create(manager),
                new Tables(manager),
                new Clear(manager),
                new Drop(manager),
                new Find(manager),
                new Insert(manager),
                new Update(manager),
                new Delete(manager),
                new Help(view),
                new Exit(view),
                new Unsupported(view)
        };
    }

    public void run() {
        try {
            connect();
            doWork();
        } catch (ExitException e) {
            // do nothing
        }
    }

    private void doWork() {

        while (true) {
            view.write("Введите команду (или help для получения списка доступных команд):");
            String userInputAsString = view.read();
            userInputAsArray = userInputAsString.split("\\s\\|\\s");
            userInputAsList = Arrays.asList(userInputAsArray);

            for (Command command : commands) {
                try {
                    if (userInputAsString != null && command.canProcess(userInputAsList.get(0))) {
                        command.process(userInputAsList);
                        break;
                    }
                } catch (Exception e) {
                    if (e instanceof ExitException) {
                        throw e;
                    }
                    printError(e);
                    break;
                }
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
}

