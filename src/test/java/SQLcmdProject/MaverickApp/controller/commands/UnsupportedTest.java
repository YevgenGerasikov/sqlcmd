package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.view.ViewForSqlcmd;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnsupportedTest {
    private static Command command;
    private static ViewForSqlcmd view;
    List<String> userInputAsList = new ArrayList<>();

    @BeforeAll
    static void beforeSetup() {
        view = Mockito.mock(ViewForSqlcmd.class);
        command = new Unsupported(view);
    }

    @Test
    void canProcess() {
        boolean canProcces = command.canProcess("create7");
        assertTrue(canProcces);
    }

    @Test
    void process() {
        //when
        userInputAsList.add("create77");
        command.process(userInputAsList);
        //then, проверяем, был ли вызов заданого метода с задаными параметрами
        Mockito.verify(view).write("Несуществующая команда: [create77]");
    }
}