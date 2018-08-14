package SQLcmdProject.MaverickApp.controller;

import SQLcmdProject.MaverickApp.model.DataBaseManager;
import SQLcmdProject.MaverickApp.model.JDBC_DataBaseManager;
import SQLcmdProject.MaverickApp.view.Console;
import SQLcmdProject.MaverickApp.view.View;

public class Main {
    public static void main(String[] args) {
        View view = new Console();
        DataBaseManager manager = new JDBC_DataBaseManager();

        Controller controller = new Controller(view, manager);
        controller.run();
    }
}
