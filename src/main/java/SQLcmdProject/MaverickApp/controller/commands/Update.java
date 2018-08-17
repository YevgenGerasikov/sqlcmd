package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;

import java.util.List;

public class Update implements Command {
    private final DataBaseManager manager;
    String tableName;

    public Update(DataBaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("update");
    }

    @Override
    public void process(List<String> userInputAsList) {
        if (userInputAsList.size() % 2 != 0) {
            throw new IllegalArgumentException(String.format("Неправильный формат комманды 'update': должно быть четное " +
                    "количество параметров в строке вида 'insert | tableName | columnName1 | columnName1Value | " +
                    "columnNameN | columnNameNValue', " + "а вы ввели: '%s'", userInputAsList.size()));
        }
        tableName = userInputAsList.get(1);
        userInputAsList = userInputAsList.subList(2, userInputAsList.size());
        manager.updateData(tableName, userInputAsList);
    }
}
