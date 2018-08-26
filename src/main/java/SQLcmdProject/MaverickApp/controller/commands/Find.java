package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;

import java.util.List;

public class Find implements Command {
    private final DataBaseManager manager;
    String tableName;

    public Find(DataBaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("find");
    }

    @Override
    public void process(List<String> userInputAsList) {
        if (userInputAsList.size() != 2) {
            throw new IllegalArgumentException(String.format("Неправильный формат комманды 'find': должно быть два " +
                    "параметра в строке вида 'find | tableName', а вы ввели: '%s'", userInputAsList.size()));
        }
        tableName = userInputAsList.get(1);
        manager.printTableToConsole(tableName);
    }
}
