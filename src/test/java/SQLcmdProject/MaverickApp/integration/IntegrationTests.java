package SQLcmdProject.MaverickApp.integration;

import SQLcmdProject.MaverickApp.controller.Main;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;


public class IntegrationTests {
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    private String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @BeforeEach
    void setupBeforeEachTest() {
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void closeAfterEachTest() {
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
        int randomNumber = (int) (Math.random() * 1000 + 31);
        String tableName = "testTableNumber" + randomNumber;
        in.add("connect | postgres | postgres | postgres");
        in.add("create | " + tableName + " | firstColumn | secondColumn");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Соединение с базой данных успешно установлено.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "Таблица '" + tableName + "' создана.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "postgres");
             Statement stmt = connection.createStatement()
        ) {
            stmt.executeUpdate("DROP TABLE IF EXISTS public." + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "postgres");
             Statement stmt = connection.createStatement()
        ) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS public.tableForClearData (id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))");
            stmt.executeUpdate("INSERT INTO public.tableForClearData(name, password) VALUES('TestNameForDelete', '123456')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        in.add("connect | postgres | postgres | postgres");
        in.add("clear | tableForClearData");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Соединение с базой данных успешно установлено.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "Содержимое таблицы 'tableForClearData' полностью очищено. Удалено 1 строк.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    void dropTest() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "postgres");
             Statement stmt = connection.createStatement()
        ) {
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
        in.add("connect | postgres | postgres | postgres");
        in.add("find | statictable");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Соединение с базой данных успешно установлено.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "Печать 5 строк из таблицы statictable\r\n" +
                "+--------------+---------------+\r\n" +
                "|     name     |    surname    |\r\n" +
                "+--------------+---------------+\r\n" +
                "| StaticName   | StaticSurname |\r\n" +
                "+--------------+---------------+\r\n" +
                "| StaticName2  | StaticSurname |\r\n" +
                "+--------------+---------------+\r\n" +
                "| StaticName3  | StaticSurname |\r\n" +
                "+--------------+---------------+\r\n" +
                "| StaticName4  | StaticSurname |\r\n" +
                "+--------------+---------------+\r\n" +
                "| StaticName5  | StaticSurname |\r\n" +
                "+--------------+---------------+\r\n" +
                "\r\n" +
                "Данные из таблицы 'statictable' выведены на экран\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    void findTestWithLimitAndOffset() {
        in.add("connect | postgres | postgres | postgres");
        in.add("find | statictable | LIMIT | 2 | OFFSET | 2");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Соединение с базой данных успешно установлено.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "Печать 2 строк из таблицы statictable\r\n" +
                "+--------------+---------------+\r\n" +
                "|     name     |    surname    |\r\n" +
                "+--------------+---------------+\r\n" +
                "| StaticName3  | StaticSurname |\r\n" +
                "+--------------+---------------+\r\n" +
                "| StaticName4  | StaticSurname |\r\n" +
                "+--------------+---------------+\r\n" +
                "\r\n" +
                "Данные из таблицы 'statictable' выведены на экран\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    void findNotExistTableTest() {//TODO test doesn't work properly
//        in.add("connect | postgres | postgres | postgres");
//        in.add("find | statictable2");
//        in.add("exit");
//        Main.main(new String[0]);
//        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
//                "Соединение с базой данных успешно установлено.\r\n" +
//                "Введите команду (или help для получения списка доступных команд):\r\n" +
//                "Таблица с заданым именем не найдена. ОШИБКА: отношение \"public.statictable2\" не существует\r\n" +
//                "  Позиция: 15\r\n" +
//                "Таблицы базы данных:\r\n" +
//                "- tableforcleardata\r\n" +
//                "- statictable\r\n" +
//                "- users\r\n" +
//                "Несуществующая команда: [find, statictable2]\r\n" +
//                "Введите команду (или help для получения списка доступных команд):\r\n" +
//                "До скорой встречи!\r\n", getData());
    }

    @Test
    void insertTest() {
        int randomNumber = (int) (Math.random() * 1000 + 31);
        String randomPassword = "randomPass" + randomNumber;
        in.add("connect | postgres | postgres | postgres");
        in.add("insert | users | name | Pavlo" + randomNumber + " | password | " + randomPassword);
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
        int randomNumber = (int) (Math.random() * 1000 + 31);
        String randomPassword = "randomPass" + randomNumber;
        in.add("connect | postgres | postgres | postgres");
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "postgres");
             Statement stmt = connection.createStatement()
        ) {
            stmt.executeUpdate("INSERT INTO public.users(name, password) VALUES ('Zina" + randomNumber + "', 'Z725@')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        in.add("update | users | password | Z725@ | password | " + randomPassword + "");
        in.add("exit");
        Main.main(new String[0]);
        assertEquals("Для подключения к соответствующей БД введите команду: connect | database | username | password\r\n" +
                "Соединение с базой данных успешно установлено.\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "Обновлено 1 строк\r\n" +
                "Введите команду (или help для получения списка доступных команд):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    void deleteTest() {
        in.add("connect | postgres | postgres | postgres");
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "postgres");
             Statement stmt = connection.createStatement()
        ) {
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
