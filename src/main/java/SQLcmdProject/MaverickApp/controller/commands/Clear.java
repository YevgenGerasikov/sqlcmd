package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;

import java.util.List;

public class Clear implements Command {
    private final DataBaseManager manager;
    String tableName;

    public Clear(DataBaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("clear");
    }

    @Override
    public void process(List<String> userInputAsList) {
        if (userInputAsList.size() != 2) {
            throw new IllegalArgumentException(String.format("Неправильный формат комманды 'clear': должно быть два " +
                    "параметра в строке вида 'clear | tableName', " + "а вы ввели: '%s'", userInputAsList.size()));
        }
        tableName = userInputAsList.get(1);
        manager.deleteAllData(tableName);
    }
}
