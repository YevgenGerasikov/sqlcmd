package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;

import java.util.List;

public class Delete implements Command {
    private final DataBaseManager manager;
    String tableName;

    public Delete(DataBaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("delete");
    }

    @Override
    public void process(List<String> userInputAsList) {
        if (userInputAsList.size() % 2 != 0) {
            throw new IllegalArgumentException(String.format("Неправильный формат комманды 'delete': должно быть четное " +
                    "количество параметров в строке вида 'delete | tableName | ColumnName | ColumnValue', " +
                    "а вы ввели: '%s'", userInputAsList.size()));
        }
        tableName = userInputAsList.get(1);
        userInputAsList = userInputAsList.subList(2, userInputAsList.size());
        manager.deleteSelectedData(tableName, userInputAsList);
    }
}
