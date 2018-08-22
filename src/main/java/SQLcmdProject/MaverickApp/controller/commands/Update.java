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
        if (userInputAsList.size() % 2 != 0 || userInputAsList.size() < 6) {
            throw new IllegalArgumentException(String.format("Неправильный формат комманды 'update': должно быть четное " +
                    "количество параметров в строке вида 'update | tableName | WhereColumnName | WhereColumnValue | " +
                    "SetColumnName | SetColumnValue', " + "а вы ввели: '%s'", userInputAsList.size()));
        }
        tableName = userInputAsList.get(1);
        userInputAsList = userInputAsList.subList(2, userInputAsList.size());
        manager.updateData(tableName, userInputAsList);
    }
}
