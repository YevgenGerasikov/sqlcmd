package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import SQLcmdProject.MaverickApp.view.ViewForSqlcmd;

import java.util.List;

public class IsConnected implements Command {
    private DataBaseManager manager;
    private ViewForSqlcmd view;

    public IsConnected(DataBaseManager manager, ViewForSqlcmd view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return !manager.isConnected();
    }

    @Override
    public void process(List<String> userInputAsList) {
        view.write("Вы не можете пользоваться командой '" + userInputAsList.get(0) + "' пока " +
                "не подключитесь с помощью комманды " + "connect | database | username | password");
    }
}
