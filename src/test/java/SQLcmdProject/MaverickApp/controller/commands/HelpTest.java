package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import SQLcmdProject.MaverickApp.view.ViewForSqlcmd;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

class HelpTest {
    private static Command command;
    private static ViewForSqlcmd view;
    List<String> userInputAsList = new ArrayList<>();

    @BeforeAll
    static void beforeSetup() {
        view = Mockito.mock(ViewForSqlcmd.class);
        command = new Help(view);
    }

    @Test
    void canProcess() {
        boolean canProcces = command.canProcess("help");
        assertTrue(canProcces);
    }

    @Test
    void process() {
        //when
        userInputAsList.add("help");
        command.process(userInputAsList);
        //then, проверяем ввывод на печать списка команд в 'help'
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view, atLeastOnce()).write(captor.capture());
        assertEquals("[Существующие команды:, \tconnect, \t\t•\tДля подключения к соответствующей БД введите" +
                " команду: connect | database | username | password, \ttables, \t\t•\tКоманда выводит список всех " +
                "таблиц базы данных, к которой подключились, \tclear | tableName, \t\t•\tКоманда очищает все содержимое " +
                "указанной 'tableName' таблицы, \tdrop | tableName, \t\t•\tКоманда удаляет заданную 'tableName' таблицу, " +
                "\tcreate | tableName | columnNameN | columnNameN, \t\t•\tКоманда создает новую таблицу с именем " +
                "'tableName' и необходимых колонок с заданными именами 'columnName', \tfind | tableName, " +
                "\t\t•\tКоманда для получения содержимого (вывод на экран) указанной таблицы 'tableName', \tinsert | " +
                "tableName | columnName1 | columnName1Value | columnNameN | columnNameNValue, \t\t•\tКоманда для " +
                "вставки одной строки в заданную таблицу 'tableName' где 'columnName1' - наименования столбца, " +
                "а 'columnName1Value' - желаемое значение, \tupdate | tableName | WhereColumnName | WhereColumnValue | " +
                "SetColumnName | SetColumnValue, \t\t•\tКоманда вносит изменения со значение SetColumnValue в ячеки в " +
                "столбце SetColumnName таблицы 'tableName' где  'WhereColumnName' = 'WhereColumnValue', \tdelete | " +
                "tableName | ColumnName | ColumnValue, \t\t•\tКоманда удаляет одну или несколько записей для которых " +
                "соблюдается условие ColumnName = ColumnValue, \thelp, \t\t•\tдля вывода этого списка на экран, \texit, " +
                "\t\t•\tдля выхода из программы]", captor.getAllValues().toString());
    }
}