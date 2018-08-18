package SQLcmdProject.MaverickApp.integration;

import SQLcmdProject.MaverickApp.controller.Main;
import SQLcmdProject.MaverickApp.model.DataBaseManager;
import SQLcmdProject.MaverickApp.model.JDBC_DataBaseManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@TestInstance(PER_METHOD)
public class IntegrationTests {
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;
    private DataBaseManager manager;
    private static Connection connection;
    private static Statement stmt;

    public String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @BeforeEach
    void setup() {
        manager = new JDBC_DataBaseManager();
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void closeAll() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void connectTest() {
        in.add("connect | postgres | postgres | postgres");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Соединение с базой данных успешно установлено.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    void helpTest() {
        in.add("help");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Существующие команды:\r\n" +
                "\tconnect\r\n" +
                "\t\t•\tДля подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "\ttables\r\n" +
                "\t\t•\tКоманда выводит список всех таблиц базы данных, к которой подключились\r\n" +
                "\tclear | tableName\r\n" +
                "\t\t•\tКоманда очищает все содержимое указанной 'tableName' таблицы\r\n" +
                "\tdrop | tableName\r\n" +
                "\t\t•\tКоманда удаляет заданную 'tableName' таблицу\r\n" +
                "\tcreate | tableName | columnNameN | columnNameN\r\n" +
                "\t\t•\tКоманда создает новую таблицу с именем 'tableName' и необходимых колонок с заданными именами 'columnName'\r\n" +
                "\tfind | tableName\r\n" +
                "\t\t•\tКоманда для получения содержимого (вывод на экран) указанной таблицы 'tableName'\r\n" +
                "\tinsert | tableName | columnName1 | columnName1Value | columnNameN | columnNameNValue\r\n" +
                "\t\t•\tКоманда для вставки одной строки в заданную таблицу 'tableName' где 'columnName1' - наименования столбца, а 'columnName1Value' - желаемое значение\r\n" +
                "\tupdate | tableName | WhereColumnName | WhereColumnValue | SetColumnName | SetColumnValue\r\n" +
                "\t\t•\tКоманда вносит изменения со значение SetColumnValue в ячеки в столбце SetColumnName таблицы 'tableName' где  'WhereColumnName' = 'WhereColumnValue'\r\n" +
                "\tdelete | tableName | ColumnName | ColumnValue\r\n" +
                "\t\t•\tКоманда удаляет одну или несколько записей для которых соблюдается условие ColumnName = ColumnValue\r\n" +
                "\thelp\r\n" +
                "\t\t•\tдля вывода этого списка на экран\r\n" +
                "\texit\r\n" +
                "\t\t•\tдля выхода из программы\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    void exitTest() {
        // given
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username |" +
                " password\r\n" + "До скорой встречи!\r\n", getData());
    }

    @Test
    void isConnectedTest() {
        in.add("tables");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Вы не можете пользоваться командой 'tables' пока не подключитесь с помощью комманды connect | database | username | password\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    void createTest() {
        in.add("connect | postgres | postgres | postgres");
        in.add("create | testTableNumber127 | firstColumn | secondColumn");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Соединение с базой данных успешно установлено.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "Таблица 'testTableNumber127' создана.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    void tablesTest() {
        in.add("connect | staticDB | postgres | postgres");
        in.add("tables");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Соединение с базой данных успешно установлено.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "Таблицы базы данных:\r\n" +
                "- static_users\r\n" +
                "- static_blanc_table\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    void clearTest() {
        in.add("connect | postgres | postgres | postgres");
        in.add("clear | test");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Соединение с базой данных успешно установлено.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "Содержимое таблицы 'test' полностью очищено. Удалено 0 строк.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    void dropTest() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
            stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tableForDelete()");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        in.add("connect | postgres | postgres | postgres");
        in.add("drop | tableForDelete");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Соединение с базой данных успешно установлено.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "Таблица 'tableForDelete' удалена.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    void findTest() {
        in.add("connect | staticDB | postgres | postgres");
        in.add("find | static_users");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Соединение с базой данных успешно установлено.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "id: 1 name: Petro surname: Petrov \r\n" +
                "--------------------------------\r\n" +
                "id: 2 name: Yevgen surname: Ivanov \r\n" +
                "--------------------------------\r\n" +
                "Данные из таблицы 'static_users' выведены на экран\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    void insertTest() {
        in.add("connect | postgres | postgres | postgres");
        in.add("insert | users | name | Pavlo | password | qwe127");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Соединение с базой данных успешно установлено.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "Добавлено 1 строк\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    void updateTest() {
        in.add("connect | postgres | postgres | postgres");
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
            stmt = connection.createStatement();
            stmt.executeUpdate("INSERT INTO public.users(name, password) VALUES ('Zina', 'Z725@')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        in.add("update | users | name | Zina | password | newPassword");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Соединение с базой данных успешно установлено.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "Обновлено 1 строк\r\n" +
                "Метод изменения записи в БД выполнен\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    void deleteTest() {
        in.add("connect | postgres | postgres | postgres");
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
            stmt = connection.createStatement();
            stmt.executeUpdate("INSERT INTO public.users(name, password) VALUES ('Luba', '123456')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        in.add("delete | users | name | Luba");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Соединение с базой данных успешно установлено.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "Удалено 1 строк\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    void unsupportedCommandTest() {
        in.add("connect | postgres | postgres | postgres");
        in.add("exiqwqwts");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Соединение с базой данных успешно установлено.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "Несуществующая команда: [exiqwqwts]\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }
}
