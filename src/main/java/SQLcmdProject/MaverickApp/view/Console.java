package SQLcmdProject.MaverickApp.view;

import java.util.Scanner;

public class Console implements ViewForSqlcmd {
    @Override
    public void write(String message) {
        System.out.println(message);
    }

    @Override
    public String read() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
