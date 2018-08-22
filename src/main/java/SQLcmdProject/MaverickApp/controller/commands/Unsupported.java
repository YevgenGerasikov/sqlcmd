package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.view.ViewForSqlcmd;

import java.util.List;

public class Unsupported implements Command {
    private ViewForSqlcmd view;

    public Unsupported(ViewForSqlcmd view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return true;
    }

    @Override
    public void process(List<String> userInputAsList) {
        view.write("Несуществующая команда: " + userInputAsList.toString());
    }
}
