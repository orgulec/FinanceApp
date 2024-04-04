package app.financeapp.utils.exceptions;

public class BudgetLimitAlreadyExistException extends RuntimeException {
    public BudgetLimitAlreadyExistException(String description) {
        super(description);
    }
}
