package SQLcmdProject.MaverickApp.model;

import SQLcmdProject.MaverickApp.model.tablePrint.DBTablePrinter;

import java.sql.*;
import java.util.List;

// Задача - привести максимум методов к использованию шаблона запроса без явного указания значений

public class JDBC_DataBaseManager implements DataBaseManager {

    Connection connection;
    private UserInputHandler userInputHandler;

    public JDBC_DataBaseManager() {
        userInputHandler = new UserInputHandler();
    }

    //создаем соединени с БД
    @Override
    public void connectToDatabase(String db_name, String db_username, String db_password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Не удалось загрузить класс драйвера базы данных.");
        }
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + db_name + "?loggerLevel=OFF", db_username, db_password);
            if (!connection.isClosed()) {
                System.out.println("Соединение с базой данных успешно установлено.");
            }
        } catch (
                SQLException e) {
            System.out.println("Не удалось установить соединение с БД. Проверьте правильность вводимых данных и повторите ввод");
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    //addTable() метод для добавления новой таблицы - CREATE [table]
    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void addTable(String tableName, List<String> userInputAsList) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(userInputHandler.createTableQuery(tableName, userInputAsList));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Таблица '" + tableName + "' создана.");
    }

    //addData() метод добавления данных в таблицу
    @Override
    public void addDataToTable(String tableName, List<String> userInputAsList) {
        try (Statement stmt = connection.createStatement()) {
            int result = stmt.executeUpdate(userInputHandler.addDataToTableQuery(tableName, userInputAsList));
            if (result != 0) {
                System.out.println("Добавлено " + result + " строк");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка в формате команды к базе данных");
            e.printStackTrace();
        }
    }

    //getTablesList() возвращает названия всех таблиц в БД
    @Override
    public void getTablesList() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(userInputHandler.getTablesListQuery());
            System.out.println("Таблицы базы данных:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("table_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //getData() метод получения данных из таблицы - READ [table]
    @Override
    public void getTableData(String tableName) {
        try (Statement stmt = connection.createStatement()) {
            //TODO realize table view of the printing table in console
            ResultSet rs = stmt.executeQuery(userInputHandler.getTableDataQuery(tableName));
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    System.out.print(rsmd.getColumnName(i) + ": " + rs.getString(i) + " "); //Print one element of a row
                }
                System.out.println();
                System.out.println("--------------------------------");

//                System.out.println("id:" + rs.getString("id"));
//                System.out.println("name:" + rs.getString("name"));
//                System.out.println("password:" + rs.getString("password"));
            }
            System.out.println("Данные из таблицы '" + tableName + "' выведены на экран");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printTableToConsole(String tableName) {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(userInputHandler.getTableDataQuery(tableName));
            DBTablePrinter dbTablePrinter = new DBTablePrinter();
            dbTablePrinter.printResultSet(rs);
            System.out.println("Данные из таблицы '" + tableName + "' выведены на экран");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //updateData() метод вносит изменения в существующую таблицу
    @Override
    public void updateData(String tableName, List<String> userInputAsList) {
        try (Statement stmt = connection.createStatement()) {
//        pstmt = connection.prepareStatement("UPDATE public.users SET password = ? WHERE id = 3");
//        String pass = "password_" + new Random().nextInt();
//        pstmt.setString(1, pass);
//        pstmt.executeUpdate();
//        pstmt.close();
            int result = stmt.executeUpdate(userInputHandler.updateDataInTableQuery(tableName, userInputAsList));
            if (result != 0) {
                System.out.println("Обновлено " + result + " строк");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //deleteSelectedData() удаляет данные из таблицы по фильтру
    @Override
    public void deleteSelectedData(String tableName, List<String> userInputAsList) {
        try (Statement stmt = connection.createStatement()) {
            int result = stmt.executeUpdate(userInputHandler.deleteDataInTableQuery(tableName, userInputAsList));
            if (result != 0) {
                System.out.println("Удалено " + result + " строк");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAllData(String tableName) {
        int result = 0;
        try (Statement stmt = connection.createStatement()) {
            result = stmt.executeUpdate(userInputHandler.deleteAllDataInTableQuery(tableName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Содержимое таблицы '" + tableName + "' полностью очищено. Удалено " + result + " строк.");
    }

    @Override
    public void deleteTable(String tableName) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(userInputHandler.deleteTableQuery(tableName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Таблица '" + tableName + "' удалена.");
    }

    //TODO transfer all methods for use prepared requests type
}

