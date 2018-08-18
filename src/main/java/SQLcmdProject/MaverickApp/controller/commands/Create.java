package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;

import java.util.List;

public class Create implements Command {
    //TODO нет проверки на создание дубликата, сообщение об успехе может дезинформировать
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
        if (userInputAsList.size() % 2 != 0) {
            throw new IllegalArgumentException(String.format("Неправильный формат комманды 'create': должно быть четное " +
                    "количество параметров в строке вида 'create | tableName | columnNameN | columnNameN', " +
                    "а вы ввели: '%s'", userInputAsList.size()));
        }
        tableName = userInputAsList.get(1);
        userInputAsList = userInputAsList.subList(2, userInputAsList.size());
        manager.addTable(tableName, userInputAsList);
    }
}
