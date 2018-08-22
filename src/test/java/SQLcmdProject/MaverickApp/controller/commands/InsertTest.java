package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InsertTest {
    private static Command command;
    private static DataBaseManager manager;
    List<String> userInputAsList = new ArrayList<>();

    @BeforeAll
    static void beforeSetup() {
        manager = Mockito.mock(DataBaseManager.class);
        command = new Insert(manager);
    }

    @Test
    void canProcess() {
        boolean canProcces = command.canProcess("insert");
        assertTrue(canProcces);
    }

    @Test
    void processWrongCommandFormat() {
        //when
        userInputAsList.add("insert");
        userInputAsList.add("newTableName");
        userInputAsList.add("columnName");
        try {
            command.process(userInputAsList);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Неправильный формат комманды 'insert': должно быть четное количество параметров в " +
                    "строке вида 'insert | tableName | columnName1 | columnName1Value | columnNameN | columnNameNValue', " +
                    "а вы ввели: '3'", e.getMessage());
        }
    }

    @Test
    void process() {
        //when
        userInputAsList.add("create");
        userInputAsList.add("newTable");
        userInputAsList.add("Column1");
        userInputAsList.add("Column2");
        command.process(userInputAsList);
        //then, проверяем, был ли вызов заданого метода с задаными параметрами
        Mockito.verify(manager).addDataToTable(userInputAsList.get(1), userInputAsList.subList(2, userInputAsList.size()));
    }
}