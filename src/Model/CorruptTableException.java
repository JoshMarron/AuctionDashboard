package Model;

public class CorruptTableException extends Exception {

    public CorruptTableException() {
        super();
    }

    public CorruptTableException(String message) {
        super(message);
    }

    public CorruptTableException(String message, Throwable cause) {
        super(message, cause);
    }

    public CorruptTableException(Throwable cause) {
        super(cause);
    }
}

