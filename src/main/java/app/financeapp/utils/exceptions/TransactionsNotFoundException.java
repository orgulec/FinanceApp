package app.financeapp.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransactionsNotFoundException extends RuntimeException {
    public TransactionsNotFoundException(String description) {
        super(description);
    }
}
