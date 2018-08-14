package SQLcmdProject.MaverickApp.model;


import java.sql.*;
import java.util.Random;


public class FirstStepProcedureStyle {
    //URL к базе состоит из протокола:подпротокола://[хоста]:[порта_СУБД]/[БД] и других_сведений
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    //Имя пользователя БД
    private static final String DB_NAME = "postgres";
    //Пароль к БД
    private static final String DB_PASSWORD = "postgres";

    //Обявляем переменную соединения типа Connection
    static Connection connection;
    static Statement stmt;
    static PreparedStatement pstmt;

    public static void main(String[] argv) throws ClassNotFoundException, SQLException {
        connectToDatabase(DB_URL, DB_NAME, DB_PASSWORD); // метод для соединения с БД
        addTable(); //метод для добавления новой таблицы - CREATE [table]
        addData(); //метод добавления данных в таблицу
        getData();//метод получения данных из таблицы - READ [table]
        getTablesList();//возвращает названия всех таблиц в БД
        deleteData(); //удаляет данные из таблицы по фильтру
        getData();//метод получения данных из таблицы - READ [table]
        updateData();//метод вносит изменения в существующую таблицу
        getData();//метод получения данных из таблицы - READ [table]
        connection.close();


    }

    static void connectToDatabase(String url, String name, String password) {
        //создаем соединени с БД
        try {
            //Загружаем драйвер
            Class.forName("org.postgresql.Driver");

            //Создаём соединение
            connection = DriverManager.getConnection(DB_URL, DB_NAME, DB_PASSWORD);

            //Информируем пользователя об открытии соединения
            if (!connection.isClosed()) {
                System.out.println("Соединение с базой данных успешно установлено.");
            }
            //закрываем соединение
            //connection.close();
            //информируем пользователя о закрытии соединения
            if (connection.isClosed()) {
                System.out.println("Соединение с базой данных успешно закрыто.");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Не удалось загрузить класс драйвера базы данных.");
        } catch (SQLException e) {
            System.out.println("Не удалось установить соединение с базой данных.");
        }
    }

    static void addTable() {
        // CREATE
        //метод добавляет новую таблицу в БД
        try {
            //создаем екземпляр запроса в базу данных
            Statement stmt = connection.createStatement();
            //пишем postresql запрос на добавление таблицы, если такая не существует
            String createTableQuery = "CREATE TABLE IF NOT EXISTS public.users (id serial PRIMARY KEY, name VARCHAR(25), password VARCHAR(25))";
            stmt.executeUpdate(createTableQuery);
            stmt.close();
            System.out.println("Таблица создана");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addData() throws SQLException {
        // UPDATE, insert
        //создаем екземпляр запроса в базу данных
        stmt = connection.createStatement();
        //пишем postresql запрос
        String sqlInsert = "INSERT INTO public.users(name, password) VALUES('papa', '123456')";
        stmt.executeUpdate(sqlInsert);//
        stmt.close();
        System.out.println("Запись добавлена");
    }

    static void getTablesList() throws SQLException {
        // table names
        //возвращает названия всех таблиц в БД
        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'");
        System.out.println("Таблицы базы данных:");
        while (rs.next()) {
            System.out.println("-" + rs.getString("table_name"));
        }
        rs.close();
        stmt.close();
    }

    static void getData() throws SQLException {
        // select, READ
        //метод возвращает содержание таблицы
        //создаем екземпляр запроса в базу данных
        stmt = connection.createStatement();
        //пишем postresql запрос типа ResultSet
        ResultSet rs = stmt.executeQuery("SELECT * FROM public.users");
        while (rs.next()) {
            System.out.println("id:" + rs.getString("id"));
            System.out.println("name:" + rs.getString("name"));
            System.out.println("password:" + rs.getString("password"));
            System.out.println("-----");
        }
        rs.close();
        stmt.close();
        System.out.println("Данные о таблице выведены на экран");
    }

    static void updateData() throws SQLException {
        // update
        //метод вносит изменения в существующую таблицу
        pstmt = connection.prepareStatement(
                "UPDATE public.users SET password = ? WHERE id = 3");
        String pass = "password_" + new Random().nextInt();
        pstmt.setString(1, pass);
        pstmt.executeUpdate();
        pstmt.close();
    }

    static void deleteData() throws SQLException {
        // delete
        //метод удалает данные из таблицы
        stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM public.users " +
                "WHERE id > 4 AND id < 100");
        stmt.close();
        System.out.println("Данные из таблицы удалены");
    }
}
