package app.financeapp.utils.exceptions;

public class IncorrectBalanceValueException extends RuntimeException {
    public IncorrectBalanceValueException(String error) {
        super(error);
    }
}
