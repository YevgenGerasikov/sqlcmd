package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DropTest {
    private static Command command;
    private static DataBaseManager manager;
    List<String> userInputAsList = new ArrayList<>();

    @BeforeAll
    static void beforeSetup() {
        manager = Mockito.mock(DataBaseManager.class);
        command = new Drop(manager);
    }

    @Test
    void canProcess() {
        boolean canProcces = command.canProcess("drop");
        assertTrue(canProcces);
    }

    @Test
    void processWrongCommandFormat() {
        //when
        userInputAsList.add("drop");
        userInputAsList.add("newTableName");
        userInputAsList.add("qqw");
        try {
            command.process(userInputAsList);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Неправильный формат комманды 'drop': должно быть два параметра в строке вида" +
                    " 'drop | tableName', а вы ввели: '3'", e.getMessage());
        }
    }

    @Test
    void process() {
        //when
        userInputAsList.add("drop");
        userInputAsList.add("tableForDrop");
        command.process(userInputAsList);
        //then, проверяем, был ли вызов заданого метода с задаными параметрами
        Mockito.verify(manager).deleteTable(userInputAsList.get(1));
    }
}