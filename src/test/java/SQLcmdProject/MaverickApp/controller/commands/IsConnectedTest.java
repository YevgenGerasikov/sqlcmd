package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import SQLcmdProject.MaverickApp.view.ViewForSqlcmd;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IsConnectedTest {
    private static Command command;
    private static DataBaseManager manager;
    private static ViewForSqlcmd view;
    List<String> userInputAsList = new ArrayList<>();

    @BeforeAll
    static void beforeSetup() {
        manager = Mockito.mock(DataBaseManager.class);
        view = Mockito.mock(ViewForSqlcmd.class);
        command = new IsConnected(manager, view);
    }

    @Test
    void canProcess() {
        boolean canProcces = command.canProcess("create");
        assertTrue(canProcces);
    }

    @Test
    void process() {
        //when
        userInputAsList.add("create");
        command.process(userInputAsList);
        Mockito.verify(view).write("Вы не можете пользоваться командой 'create' пока не подключитесь с помощью комманды" +
                " connect | database | username | password");
    }
}