package SQLcmdProject.MaverickApp.controller.commands;

import java.util.List;

public interface Command {
    boolean canProcess(String command);

    void process(List<String> userInputAsList);
}
