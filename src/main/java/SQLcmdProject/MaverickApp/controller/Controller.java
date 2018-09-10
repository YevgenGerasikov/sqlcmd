package SQLcmdProject.MaverickApp.controller;

import SQLcmdProject.MaverickApp.controller.commands.*;
import SQLcmdProject.MaverickApp.model.DataBaseManager;
import SQLcmdProject.MaverickApp.model.NotExistTableNameException;
import SQLcmdProject.MaverickApp.view.ViewForSqlcmd;

import java.util.Arrays;
import java.util.List;

public class Controller {
    private ViewForSqlcmd view;
    private DataBaseManager manager;
    private String[] userInputAsArray;
    private List<String> userInputAsList;
    private Command[] commands;

    public Controller(ViewForSqlcmd view, DataBaseManager manager) {
        this.view = view;
        this.manager = manager;
        this.commands = new Command[]{
                new Connect(manager),
                new Help(view),
                new Exit(view),
                new IsConnected(manager, view),
                new Create(manager),
                new Tables(manager),
                new Clear(manager),
                new Drop(manager),
                new Find(manager),
                new Insert(manager),
                new Update(manager),
                new Delete(manager),
                new Unsupported(view)
        };
    }

    public void run() {
        try {
            doWork();
        } catch (ExitException e) {
            // do nothing
        }
    }

    private void doWork() {
        view.write("Для подключения к соответствующей БД введите команду: connect | database | username | password");
        while (true) {
            String userInputAsString = view.read();
            userInputAsArray = userInputAsString.split("\\s+\\|\\s+");
            userInputAsList = Arrays.asList(userInputAsArray);
            for (Command command : commands) {
                try {
                    if (userInputAsString != null && command.canProcess(userInputAsList.get(0))) {
                        command.process(userInputAsList);
                        break;
                    }
                } catch (NotExistTableNameException e) {
                    System.out.println(e.getMessage());
                    commands[5].process(userInputAsList);
                } catch (Exception e) {
                    if (e instanceof ExitException) {
                        throw e;
                    }
                    printError(e);
                    break;
                }
            }
            view.write("Введите команду (или help для получения списка доступных команд):");
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

