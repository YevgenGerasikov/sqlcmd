package SQLcmdProject.MaverickApp.controller;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import SQLcmdProject.MaverickApp.model.JDBC_DataBaseManager;
import SQLcmdProject.MaverickApp.view.Console;
import SQLcmdProject.MaverickApp.view.ViewForSqlcmd;

public class Main {
    public static void main(String[] args) {
        ViewForSqlcmd view = new Console();
        DataBaseManager manager = new JDBC_DataBaseManager();

        Controller controller = new Controller(view, manager);
        controller.run();
        String connect = "connect | postgres | postgres | postgres";
        String connect2 = "connect | staticDB | postgres | postgres";
    }
}
