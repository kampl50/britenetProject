package exceptions;

public class FileSizeToLargeException extends Exception {
    public FileSizeToLargeException(String message) {
        super(message);
    }
}
