package app.financeapp.utils.exceptions;

public class NoTransactionFoundException extends RuntimeException {
    public NoTransactionFoundException(String description) {
        super(description);
    }
}
