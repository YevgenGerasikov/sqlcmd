package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateTest {
    private static Command command;
    private static DataBaseManager manager;
    List<String> userInputAsList = new ArrayList<>();

    @BeforeAll
    static void beforeSetup() {
        manager = Mockito.mock(DataBaseManager.class);
        command = new Create(manager);
    }

    @Test
    void canProcess() {
        boolean canProcces = command.canProcess("create");
        assertTrue(canProcces);
    }

    @Test
    void processWrongCommandFormat() {
        //when
        userInputAsList.add("create");
        userInputAsList.add("newTableName");
        try {
            command.process(userInputAsList);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Неправильный формат комманды 'create': должно быть больше '2' парметров" +
                    " в строке  вида 'create | tableName | columnName1 | columnNameN', а вы ввели: '2'", e.getMessage());
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
        Mockito.verify(manager).addTable(userInputAsList.get(1), userInputAsList.subList(2, userInputAsList.size()));
    }
}