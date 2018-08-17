package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.view.ViewForSqlcmd;

import java.util.List;

public class Exit implements Command {

    private ViewForSqlcmd view;

    public Exit(ViewForSqlcmd view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("exit");
    }

    @Override
    public void process(List<String> userInputAsList) {
        view.write("До скорой встречи!");
        throw new ExitException();
    }
}