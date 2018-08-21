package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteTest {
    private static Command command;
    private static DataBaseManager manager;
    List<String> userInputAsList = new ArrayList<>();

    @BeforeAll
    static void beforeSetup() {
        manager = Mockito.mock(DataBaseManager.class);
        command = new Delete(manager);
    }

    @Test
    void canProcess() {
        boolean canProcces = command.canProcess("delete");
        assertTrue(canProcces);
    }

    @Test
    void processWrongCommandFormat() {
        userInputAsList.add("delete");
        userInputAsList.add("tableName");
        userInputAsList.add("columnName");
        try {
            command.process(userInputAsList);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Неправильный формат комманды 'delete': должно быть четное количество " +
                    "параметров в строке вида 'delete | tableName | ColumnName | ColumnValue', " +
                    "а вы ввели: '3'", e.getMessage());
        }
    }

    @Test
    void process() {
        userInputAsList.add("delete");
        userInputAsList.add("tableName");
        userInputAsList.add("columnName");
        userInputAsList.add("columnValue");
        command.process(userInputAsList);
        //then, проверяем, был ли вызов заданого метода с задаными параметрами
        Mockito.verify(manager).deleteSelectedData(userInputAsList.get(1), userInputAsList.subList(2, userInputAsList.size()));
    }
}