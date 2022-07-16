package apt.project.backend;

/**
 * This class is a gneric error for passing a message and a status code.
 * May be extended later.
 * */
public class GenericException extends Exception {

    private final int statusCode;
    private final String message;

    public GenericException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }
    public String getMessage() {
        return message;
    }

}
