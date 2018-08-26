package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FindTest {
    private static Command command;
    private static DataBaseManager manager;
    List<String> userInputAsList = new ArrayList<>();

    @BeforeAll
    static void beforeSetup() {
        manager = Mockito.mock(DataBaseManager.class);
        command = new Find(manager);
    }

    @Test
    void canProcess() {
        boolean canProcces = command.canProcess("find");
        assertTrue(canProcces);
    }

    @Test
    void processWrongCommandFormat() {
        //when
        userInputAsList.add("find");
        try {
            command.process(userInputAsList);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Неправильный формат комманды 'find': должно быть два параметра в строке вида " +
                    "'find | tableName', а вы ввели: '1'", e.getMessage());
        }
    }

    @Test
    void process() {
        //when
        userInputAsList.add("find");
        userInputAsList.add("tableName");
        command.process(userInputAsList);
        //then, проверяем, был ли вызов заданого метода с задаными параметрами
        Mockito.verify(manager).printTableToConsole(userInputAsList.get(1));
    }
}