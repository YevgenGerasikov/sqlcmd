package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import SQLcmdProject.MaverickApp.view.ViewForSqlcmd;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExitTest {
    private static Command command;
    private static ViewForSqlcmd view;
    List<String> userInputAsList = new ArrayList<>();

    @BeforeAll
    static void beforeSetup() {
        view = Mockito.mock(ViewForSqlcmd.class);
        command = new Exit(view);
    }

    @Test
    void canProcess() {
        boolean canProcces = command.canProcess("exit");
        assertTrue(canProcces);
    }

    @Test
    void process() {
        //when
        userInputAsList.add("exit");
        try {
            command.process(userInputAsList);
            Mockito.verify(view).write("До скорой встречи!");
            fail("Exception not thrown");
        } catch (ExitException e) {
            //then
        }
    }
}