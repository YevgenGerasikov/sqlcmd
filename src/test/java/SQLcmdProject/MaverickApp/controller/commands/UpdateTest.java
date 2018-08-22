package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UpdateTest {
    private static Command command;
    private static DataBaseManager manager;
    List<String> userInputAsList = new ArrayList<>();

    @BeforeAll
    static void beforeSetup() {
        manager = Mockito.mock(DataBaseManager.class);
        command = new Update(manager);
    }

    @Test
    void canProcess() {
        boolean canProcces = command.canProcess("update");
        assertTrue(canProcces);
    }

    @Test
    void processWrongCommandFormat() {
        //when
        userInputAsList.add("update");
        userInputAsList.add("newTableName");
        try {
            command.process(userInputAsList);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Неправильный формат комманды 'update': должно быть четное количество параметров " +
                    "в строке вида 'update | tableName | WhereColumnName | WhereColumnValue | SetColumnName | " +
                    "SetColumnValue', а вы ввели: '2'", e.getMessage());
        }
    }

    @Test
    void process() {
        //when
        userInputAsList.add("update");
        userInputAsList.add("tableName");
        userInputAsList.add("whereColumnName");
        userInputAsList.add("whereColumnValue");
        userInputAsList.add("SetColumnName");
        userInputAsList.add("SetColumnValue");
        command.process(userInputAsList);
        //then, проверяем, был ли вызов заданого метода с задаными параметрами
        Mockito.verify(manager).updateData(userInputAsList.get(1), userInputAsList.subList(2, userInputAsList.size()));
    }
}