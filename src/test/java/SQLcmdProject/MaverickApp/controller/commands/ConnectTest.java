package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConnectTest {
    private static DataBaseManager manager;
    List<String> userInputAsList = new ArrayList<>();

    @BeforeAll
    static void beforeSetup() {
        manager = Mockito.mock(DataBaseManager.class);
    }

    @Test
    void canProcess() {
        Command command = new Connect(manager);
        boolean canProcces = command.canProcess("connect");
        assertTrue(canProcces);
    }

    @Test
    void processWrongCommandFormat() {
        //given
        Command command = new Connect(manager);
        //when
        userInputAsList.add("connect");
        userInputAsList.add("postgres");
        try {
            command.process(userInputAsList);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Неправильный формат комманды 'connect': нужно ввести 4 параметра в строке" +
                    " вида 'connect | database | username | password', а вы ввели: '2'", e.getMessage());
        }
    }

    @Test
    void process() {
        //given
        Command command = new Connect(manager);
        //when
        userInputAsList.add("connect");
        userInputAsList.add("postgres");
        userInputAsList.add("postgres");
        userInputAsList.add("postgres");
        command.process(userInputAsList);
        //then, проверяем, был ли вызов заданого метода с задаными параметрами
        Mockito.verify(manager).connectToDatabase("postgres", "postgres", "postgres");
    }
}