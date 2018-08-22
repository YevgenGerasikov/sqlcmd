package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;

import java.util.List;

public class Insert implements Command {
    private final DataBaseManager manager;
    String tableName;

    public Insert(DataBaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("insert");
    }

    @Override
    public void process(List<String> userInputAsList) {
        if (userInputAsList.size() % 2 != 0 || userInputAsList.size() < 4) {
            throw new IllegalArgumentException(String.format("Неправильный формат комманды 'insert': должно быть четное " +
                    "количество параметров в строке вида 'insert | tableName | columnName1 | columnName1Value | " +
                    "columnNameN | columnNameNValue', " + "а вы ввели: '%s'", userInputAsList.size()));
        }
        tableName = userInputAsList.get(1);
        userInputAsList = userInputAsList.subList(2, userInputAsList.size());
        manager.addDataToTable(tableName, userInputAsList);
    }
}
