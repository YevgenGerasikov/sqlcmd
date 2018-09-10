package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;

import java.util.List;

public class Find implements Command {
    private final DataBaseManager manager;
    String tableName;
    String limit = "";
    String offset = "";

    public Find(DataBaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("find");
    }

    @Override
    public void process(List<String> userInputAsList) {
        if (userInputAsList.size() % 2 != 0) {
            throw new IllegalArgumentException(String.format("Неправильный формат комманды 'find': должно быть четное количество " +
                    "параметров в строке вида 'find | tableName', а вы ввели: '%s'", userInputAsList.size()));
        }
        if (userInputAsList.contains("LIMIT")) {
            limit = " LIMIT " + userInputAsList.get(userInputAsList.indexOf("LIMIT") + 1);
        }
        if (userInputAsList.contains("OFFSET")) {
            offset = " OFFSET " + userInputAsList.get(userInputAsList.indexOf("OFFSET") + 1);
        }
        tableName = userInputAsList.get(1);
        manager.printTableToConsole(tableName, limit, offset);
    }
}
