package SQLcmdProject.MaverickApp.model;

public class NotExistTableNameException extends RuntimeException {
    NotExistTableNameException(String msg) {
        super(msg);
    }
}
