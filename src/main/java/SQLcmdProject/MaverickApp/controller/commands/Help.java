package SQLcmdProject.MaverickApp.controller.commands;

import SQLcmdProject.MaverickApp.view.ViewForSqlcmd;

import java.util.List;

public class Help implements Command {
    private ViewForSqlcmd view;

    public Help(ViewForSqlcmd view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(List<String> userInputAsList) {
        view.write("Существующие команды:");

        view.write("\tconnect");
        view.write("\t\t•\tДля подключения к соответствующей БД введите команду: connect | database | username | password");

        view.write("\ttables");
        view.write("\t\t•\tКоманда выводит список всех таблиц базы данных, к которой подключились");

        view.write("\tclear | tableName");
        view.write("\t\t•\tКоманда очищает все содержимое указанной 'tableName' таблицы");

        view.write("\tdrop | tableName");
        view.write("\t\t•\tКоманда удаляет заданную 'tableName' таблицу");

        view.write("\tcreate | tableName | columnNameN | columnNameN");
        view.write("\t\t•\tКоманда создает новую таблицу с именем 'tableName' и необходимых колонок с заданными именами 'columnName'");

        view.write("\tfind | tableName");
        view.write("\t\t•\tКоманда для получения содержимого (вывод на экран) указанной таблицы 'tableName'");

        view.write("\tinsert | tableName | columnName1 | columnName1Value | columnNameN | columnNameNValue");
        view.write("\t\t•\tКоманда для вставки одной строки в заданную таблицу 'tableName' где 'columnName1' - наименования столбца, " +
                "а 'columnName1Value' - желаемое значение");

        view.write("\tupdate | tableName | WhereColumnName | WhereColumnValue | SetColumnName | SetColumnValue");
        view.write("\t\t•\tКоманда вносит изменения со значение SetColumnValue в ячеки в столбце SetColumnName таблицы 'tableName' " +
                "где  'WhereColumnName' = 'WhereColumnValue'");

        view.write("\tdelete | tableName | ColumnName | ColumnValue");
        view.write("\t\t•\tКоманда удаляет одну или несколько записей для которых соблюдается условие ColumnName = ColumnValue");

        view.write("\thelp");
        view.write("\t\t•\tдля вывода этого списка на экран");

        view.write("\texit");
        view.write("\t\t•\tдля выхода из программы");
    }
}
