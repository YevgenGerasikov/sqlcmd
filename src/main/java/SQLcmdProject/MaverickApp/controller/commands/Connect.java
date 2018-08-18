package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;

import java.util.List;

public class Connect implements Command {
    private DataBaseManager manager;

    public Connect(DataBaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("connect");
    }

    @Override
    public void process(List<String> userInputAsList) {
        final int CONNECT_QUERY_LENGTH = 4;
        if (userInputAsList.size() != CONNECT_QUERY_LENGTH) {
            throw new IllegalArgumentException(String.format("Неправильный формат комманды 'connect': нужно ввести " +
                    CONNECT_QUERY_LENGTH + " параметра в строке вида 'connect | database | username | password', " +
                    "а вы ввели: '%s'", userInputAsList.size()));
        }
        String databaseURL = userInputAsList.get(1);
        String username = userInputAsList.get(2);
        String password = userInputAsList.get(3);
        manager.connectToDatabase(databaseURL, username, password);
    }
}
