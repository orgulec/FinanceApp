package app.financeapp.utils.exceptions;

public class NoTransactionFoundedException extends RuntimeException {
    public NoTransactionFoundedException(String description) {
        super(description);
    }
}
