package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;

import java.util.List;

public class Drop implements Command {
    private final DataBaseManager manager;
    String tableName;

    public Drop(DataBaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("drop");
    }

    @Override
    public void process(List<String> userInputAsList) {
        if (userInputAsList.size() != 2) {
            throw new IllegalArgumentException(String.format("Неправильный формат комманды 'drop': должно быть два " +
                    "параметра в строке вида 'drop | tableName', " + "а вы ввели: '%s'", userInputAsList.size()));
        }
        tableName = userInputAsList.get(1);
        manager.deleteTable(tableName);
    }
}
