package SQLcmdProject.MaverickApp.model;

import java.sql.*;
import java.util.List;

// Задача - привести максимум методов к использованию шаблона запроса без явного указания значений

public class JDBC_DataBaseManager implements DataBaseManager {

    static Connection connection;
    private static Statement stmt;
    private static PreparedStatement pstmt;

    private UserInputHandler userInputHandler;

    public JDBC_DataBaseManager() {
        userInputHandler = new UserInputHandler();
    }

    //TODO перевести все методы в конструкцию "try with recources", для реализации AutoCloseable
    // https://habr.com/post/178405/

    //создаем соединени с БД
    @Override
    public void connectToDatabase(String db_name, String db_username, String db_password) {
        try {
            //Загружаем драйвер
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + db_name, db_username, db_password);
            //Информируем пользователя об открытии соединения
            if (!connection.isClosed()) {
                System.out.println("Соединение с базой данных успешно установлено.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Не удалось загрузить класс драйвера базы данных.");
        } catch (SQLException e) {
            System.out.println("Не удалось установить соединение с БД. Проверьте правильность вводимых данных и повторите ввод");
        }
    }
    //addTable() метод для добавления новой таблицы - CREATE [table]
    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void addTable(String tableName, List<String> userInputAsList) {
        try {
            stmt = connection.createStatement();
            //получаем и выполняем postresql запрос из обработчика пользовательских данных UserInputHandler
            stmt.executeUpdate(userInputHandler.createTableQuery(tableName, userInputAsList));
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Таблица '" + tableName + "' создана.");
    }
    //addData() метод добавления данных в таблицу
    @Override
    public void addDataToTable(String tableName, List<String> userInputAsList) {
        try {
            stmt = connection.createStatement();
            int result = stmt.executeUpdate(userInputHandler.addDataToTableQuery(tableName, userInputAsList));
            if (result != 0) {
                System.out.println("Добавлено " + result + " строк");
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Ошибка в формате команды к базе данных");
            e.printStackTrace();
        }
    }
    //getTablesList() возвращает названия всех таблиц в БД
    @Override
    public void getTablesList() {
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(userInputHandler.getTablesListQuery());
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
    @Override
    public void getTableData(String tableName) {
        try {
            //TODO realize table view of the printing table in console
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(userInputHandler.getTableDataQuery(tableName));
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for(int i = 1 ; i <= columnsNumber; i++){
                    System.out.print(rsmd.getColumnName(i)+": " + rs.getString(i) + " "); //Print one element of a row
                }
                System.out.println();
                System.out.println("--------------------------------");

//                System.out.println("id:" + rs.getString("id"));
//                System.out.println("name:" + rs.getString("name"));
//                System.out.println("password:" + rs.getString("password"));
            }
            rs.close();
            stmt.close();
            System.out.println("Данные из таблицы '" + tableName + "' выведены на экран");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //updateData() метод вносит изменения в существующую таблицу
    @Override
    public void updateData(String tableName, List<String> userInputAsList) {
        try {
//        pstmt = connection.prepareStatement("UPDATE public.users SET password = ? WHERE id = 3");
//        String pass = "password_" + new Random().nextInt();
//        pstmt.setString(1, pass);
//        pstmt.executeUpdate();
//        pstmt.close();
            stmt = connection.createStatement();
            int result = stmt.executeUpdate(userInputHandler.updateDataInTableQuery(tableName, userInputAsList));
            if (result != 0) {
                System.out.println("Обновлено " + result + " строк");
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //deleteSelectedData() удаляет данные из таблицы по фильтру
    @Override
    public void deleteSelectedData(String tableName, List<String> userInputAsList) {
        try {
            stmt = connection.createStatement();
            int result = stmt.executeUpdate(userInputHandler.deleteDataInTableQuery(tableName, userInputAsList));
            stmt.close();
            if (result != 0) {
                System.out.println("Удалено " + result + " строк");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void deleteAllData(String tableName){
        int result =0;
        try {
            stmt = connection.createStatement();
            result = stmt.executeUpdate(userInputHandler.deleteAllDataInTableQuery(tableName));
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Содержимое таблицы '" + tableName + "' полностью очищено. Удалено " + result + " строк.");
    }

    @Override
    public void deleteTable(String tableName){
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(userInputHandler.deleteTableQuery(tableName));
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Таблица '" + tableName + "' удалена.");
    }

    //TODO transfer all methods for use prepared requests type
}

