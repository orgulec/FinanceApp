package app.financeapp.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class IncorrectBalanceValueException extends RuntimeException {
    public IncorrectBalanceValueException(String error) {
        super(error);
    }
}
