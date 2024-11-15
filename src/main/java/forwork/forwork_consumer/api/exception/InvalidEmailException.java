package forwork.forwork_consumer.api.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message) {
        super(message);
    }

    public InvalidEmailException(Throwable cause) {
        super(cause);
    }
}