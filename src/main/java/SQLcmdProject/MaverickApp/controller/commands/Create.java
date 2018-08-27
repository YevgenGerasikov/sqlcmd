package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;

import java.util.List;

public class Create implements Command {
    private final DataBaseManager manager;
    String tableName;

    public Create(DataBaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("create");
    }

    @Override
    public void process(List<String> userInputAsList) {
        if (userInputAsList.size() < 3) {
            throw new IllegalArgumentException(String.format("Неправильный формат комманды 'create': должно быть " +
                    "больше '2' парметров в строке " +
                    " вида 'create | tableName | columnName1 | columnNameN', " +
                    "а вы ввели: '%s'", userInputAsList.size()));
        }
        tableName = userInputAsList.get(1);
        userInputAsList = userInputAsList.subList(2, userInputAsList.size());
        manager.addTable(tableName, userInputAsList);
    }
}
