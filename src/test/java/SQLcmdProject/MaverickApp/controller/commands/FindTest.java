package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FindTest {
    private static DataBaseManager manager;
    String tableName = "users";
    List<String> userInputAsList = new ArrayList<>();

    @BeforeAll
    static void beforeSetup() {
        manager = Mockito.mock(DataBaseManager.class);
    }

    @Test
    void canProcess() {


    }

    @Test
    void process() {
        //given
        userInputAsList.add("find");
        userInputAsList.add("users");
        Command command = new Find(manager);

    }
}