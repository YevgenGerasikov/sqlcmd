package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClearTest {
    private static Command command;
    private static DataBaseManager manager;
    List<String> userInputAsList = new ArrayList<>();

    @BeforeAll
    static void beforeSetup() {
        manager = Mockito.mock(DataBaseManager.class);
        command = new Clear(manager);
    }

    @Test
    void canProcess() {
        boolean canProcces = command.canProcess("clear");
        assertTrue(canProcces);
    }

    @Test
    void processWrongCommandFormat() {
        //when
        userInputAsList.add("clear");
        try {
            command.process(userInputAsList);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Неправильный формат комманды 'clear': должно быть два параметра в строке " +
                    "вида 'clear | tableName', а вы ввели: '1'", e.getMessage());
        }
    }

    @Test
    void process() {
        //when
        userInputAsList.add("clear");
        userInputAsList.add("tableForClearData");
        command.process(userInputAsList);
        //then, проверяем, был ли вызов заданого метода с задаными параметрами
        Mockito.verify(manager).deleteAllData("tableForClearData");
    }
}