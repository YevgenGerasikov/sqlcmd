package SQLcmdProject.MaverickApp;

import java.sql.*;

// Задача - привести максимум методов к использованию шаблона запроса без явного указания значений

public class JDBC_DataBaseManager {
    //Model (MVC) part
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_NAME = "postgres";
    private static final String DB_PASSWORD = "postgres";

    static Connection connection;
    static Statement stmt;
    static PreparedStatement pstmt;

    //TODO перевести все методы в конструкцию "try with recources", для реализации AutoCloseable
    // https://habr.com/post/178405/

    //создаем соединени с БД
    void connectToDatabase() {
        try {
            //Загружаем драйвер
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_NAME, DB_PASSWORD);
            //Информируем пользователя об открытии соединения
            if (!connection.isClosed()) {
                System.out.println("Соединение с базой данных успешно установлено.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Не удалось загрузить класс драйвера базы данных.");
        } catch (SQLException e) {
            System.out.println("Не удалось установить соединение с базой данных.");
        }
    }
    //addTable() метод для добавления новой таблицы - CREATE [table]
    void addTable(ArraylistDataSet myInstance) {
        try {
            stmt = connection.createStatement();
            //получаем и выполняем postresql запрос из обработчика пользовательских данных ArraylistDataSet
            int result = stmt.executeUpdate(myInstance.createTableQuery());
            if (result != 0) {
                System.out.println("Таблица создана");
            }
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //addData() метод добавления данных в таблицу
    void addDataToTable(ArraylistDataSet myInstance) {
        try {
            stmt = connection.createStatement();
            int result = stmt.executeUpdate(myInstance.addDataToTableQuery());
            if (result != 0) {
                System.out.println("Добавлено " + result + " строк");
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //getTablesList() возвращает названия всех таблиц в БД
    void getTablesList(ArraylistDataSet myInstance) {
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(myInstance.getTablesListQuery());
            System.out.println("Таблицы базы данных:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("table_name"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //getData() метод получения данных из таблицы - READ [table]
    void getTableData(ArraylistDataSet myInstance) {
        try {
            //TODO realize table view of the printing table in console
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(myInstance.getTableDataQuery());
            while (rs.next()) {
                System.out.println("id:" + rs.getString("id"));
                System.out.println("name:" + rs.getString("name"));
                System.out.println("password:" + rs.getString("password"));
                System.out.println("-----");
            }
            rs.close();
            stmt.close();
            System.out.println("Данные из таблицы '" + myInstance.getTableName() + "' выведены на экран");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //updateData() метод вносит изменения в существующую таблицу
    void updateData(ArraylistDataSet myInstance) throws SQLException {
//        pstmt = connection.prepareStatement("UPDATE public.users SET password = ? WHERE id = 3");
//        String pass = "password_" + new Random().nextInt();
//        pstmt.setString(1, pass);
//        pstmt.executeUpdate();
//        pstmt.close();
        stmt = connection.createStatement();
        int result = stmt.executeUpdate(myInstance.updateDataInTableQuery());
        if (result != 0) {
            System.out.println("Обновлено " + result + " строк");
        }
        stmt.close();
        System.out.println("Метод изменения записи в БД выполнен");
    }
    //deleteData() удаляет данные из таблицы по фильтру
    void deleteData(ArraylistDataSet myInstance) throws SQLException {
        stmt = connection.createStatement();
        int result = stmt.executeUpdate(myInstance.deleteDataInTableQuery());
        stmt.close();
        if (result != 0) {
            System.out.println("Удалено " + result + " строк");
        }
    }

    //TODO transfer all methods for use prepared requests type
    public static void executePstmtQuery(MapDataSet myMapInstance) throws SQLException {
        pstmt = connection.prepareStatement(myMapInstance.getPstmtForCreateTableQuery());
        if (myMapInstance.addTableParams.isEmpty()) {
            String dbName = "users";
            pstmt.setString(1, dbName);
        } else {
            pstmt.setString(1, myMapInstance.getDatabaseName());
            int paramNumber = 2;
            for (String paramName : myMapInstance.addTableParams.keySet()) {
                Object paramValue = myMapInstance.addTableParams.get(paramName);
                if (paramValue != null) {
                    pstmt.setString(paramNumber, myMapInstance.addTableParams.toString());
                }
                paramNumber++;
            }
        }
    }
}

