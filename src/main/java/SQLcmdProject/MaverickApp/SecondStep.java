package SQLcmdProject.MaverickApp;

import java.sql.*;

// Задача - привести максимум методов к использованию шаблона запроса без явного указания значений

public class SecondStep {
    //Model (MVC) part
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_NAME = "postgres";
    private static final String DB_PASSWORD = "postgres";

    static Connection connection;
    static Statement stmt;
    static PreparedStatement pstmt;


    public static void main(String[] argv) throws ClassNotFoundException, SQLException {
        connectToDatabase(DB_URL, DB_NAME, DB_PASSWORD); // метод для соединения с БД
//        addTable(); //метод для добавления новой таблицы - CREATE [table]
//        addData(); //метод добавления данных в таблицу
//        getData();//метод получения данных из таблицы - READ [table]
//        getTablesList();//возвращает названия всех таблиц в БД
//        deleteData(); //удаляет данные из таблицы по фильтру
//        getData();//метод получения данных из таблицы - READ [table]
//        updateData();//метод вносит изменения в существующую таблицу
//        getData();//метод получения данных из таблицы - READ [table]
        ArraylistDataSet testInstance = new ArraylistDataSet();
        testInstance.setTableName("users");
//        System.out.println("DB name = " + testInstance.getTableName());
//        System.out.println("CREATE, имитируем пользовательский ввод команды для создания таблицы");
//        testInstance.userKeysInput.add("name");
//        testInstance.userKeysInput.add("password");
//        testInstance.userValuesInput.add("VARCHAR(25)");
//        testInstance.userValuesInput.add("VARCHAR(25)");
//        System.out.println("Запрос для создания таблицы с пользовательскими данными = " + testInstance.createTableQuery());
//        System.out.println("Отправляем запрос для создания таблицы в БД");
//        addTable(testInstance);
        //очитска хранилища пользовательского ввода
        //TODO нужен метод для очитстки контейнеров перед пользовательским вводом
        testInstance.userKeysInput.clear();
        testInstance.userValuesInput.clear();
        //INSERT DATA
//        System.out.println("INSERT, имитируем пользовательский ввод команды для добавления данных в таблицу");
//        testInstance.userKeysInput.add("name");
//        testInstance.userValuesInput.add("'Vasyl140'");
//        testInstance.userKeysInput.add("password");
//        testInstance.userValuesInput.add("'12345678p'");
//        System.out.println("Команда для добавлекния данных в таблицу = " + testInstance.addDataToTableQuery());
//        addDataToTable(testInstance);
        //
        //getTablesList(testInstance);
        //getTableData(testInstance);
        //UPDATE DATA TEST
//        testInstance.userSetInputForTableEdit.add("password");
//        testInstance.userSetInputForTableEdit.add("testPASSWOD2");
//        testInstance.userSetInputForTableEdit.add("name");
//        testInstance.userSetInputForTableEdit.add("testNAME2");
//        testInstance.userWhereInputForTableEdit.add("name");
//        testInstance.userWhereInputForTableEdit.add("Petro");
//        updateData(testInstance);
        //DELETE data from table
        testInstance.userInputForDeleteInfo.add("name");
        testInstance.userInputForDeleteInfo.add("Petro24");
        deleteData(testInstance);

        connection.close();


    }
    //TODO перевести все методы в конструкцию "try with recources", для реализации AutoCloseable
    // https://habr.com/post/178405/

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

    static void addTable(ArraylistDataSet myInstance) {
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

    static void addDataToTable(ArraylistDataSet myInstance) {
        try {
            stmt = connection.createStatement();
            //получаем и выполняем postresql запрос из обработчика пользовательских данных ArraylistDataSet
            int result = stmt.executeUpdate(myInstance.addDataToTableQuery());
            if (result != 0) {
                System.out.println("Добавлено " + result + " строк");
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void getTablesList(ArraylistDataSet myInstance) {
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(myInstance.getTablesListQuery());
            System.out.println("Таблицы базы данных:");
            while (rs.next()) {
                System.out.println("-" + rs.getString("table_name"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void getTableData(ArraylistDataSet myInstance) {
        // select, READ - метод возвращает содержание таблицы
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

    static void updateData(ArraylistDataSet myInstance) throws SQLException {
        // update
        //метод вносит изменения в существующую таблицу
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

    static void deleteData(ArraylistDataSet myInstance) throws SQLException {
        // delete
        //метод удалает данные из таблицы
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

