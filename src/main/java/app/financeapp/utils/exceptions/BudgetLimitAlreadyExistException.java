package app.financeapp.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BudgetLimitAlreadyExistException extends RuntimeException {
    public BudgetLimitAlreadyExistException(String description) {
        super(description);
    }
}
