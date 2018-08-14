package SQLcmdProject.MaverickApp.model;

import java.sql.SQLException;
import java.util.List;

public interface DataBaseManager {

    //создаем соединени с БД
    void connectToDatabase(String db_url, String db_username, String db_password) throws SQLException;

    //addTable() метод для добавления новой таблицы - CREATE [table]
    void addTable(String tableName, List<String> userInputAsList);

    //addData() метод добавления данных в таблицу
    void addDataToTable(UserInputHandler myInstance);

    //getTablesList() возвращает названия всех таблиц в БД
    void getTablesList();

    //getData() метод получения данных из таблицы - READ [table]
    void getTableData(String tableName);

    //updateData() метод вносит изменения в существующую таблицу
    void updateData(UserInputHandler myInstance);

    //deleteSelectedData() удаляет данные из таблицы по фильтру
    void deleteSelectedData(UserInputHandler myInstance);

    //deleteAllData() удаляет все записи в заданой таблице
    void deleteAllData(String tableName);

    //deleteTable() удаляет таблицу с заданым именем
    void deleteTable(String tableName);
}
