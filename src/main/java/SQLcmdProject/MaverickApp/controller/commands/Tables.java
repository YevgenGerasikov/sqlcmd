package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import SQLcmdProject.MaverickApp.view.ViewForSqlcmd;

import java.util.List;

public class Tables implements Command {
    private final DataBaseManager manager;


    public Tables(DataBaseManager manager) {
        this.manager = manager;
    }


    @Override
    public boolean canProcess(String command) {
        return command.equals("tables");
    }

    @Override
    public void process(List<String> userInputAsList) {
        manager.getTablesList();
    }
}
