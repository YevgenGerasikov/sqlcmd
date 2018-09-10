package SQLcmdProject.MaverickApp.model;

import java.util.List;

public interface DataBaseManager {

    //создаем соединени с БД
    void connectToDatabase(String db_url, String db_username, String db_password);

    //addTable() метод для добавления новой таблицы - CREATE [table]
    boolean isConnected();

    //addTable() метод для добавления новой таблицы - CREATE [table]
    void addTable(String tableName, List<String> userInputAsList);

    //addData() метод добавления данных в таблицу
    void addDataToTable(String tableName, List<String> userInputAsList);

    //getTablesList() возвращает названия всех таблиц в БД
    void getTablesList();

    //getData() метод получения данных из таблицы - READ [table]
    void getTableData(String tableName);

    void printTableToConsole(String tableName, String limit, String offset);

    //updateData() метод вносит изменения в существующую таблицу
    void updateData(String tableName, List<String> userInputAsList);

    //deleteSelectedData() удаляет данные из таблицы по фильтру
    void deleteSelectedData(String tableName, List<String> userInputAsList);

    //deleteAllData() удаляет все записи в заданой таблице
    void deleteAllData(String tableName);

    //deleteTable() удаляет таблицу с заданым именем
    void deleteTable(String tableName);
}
