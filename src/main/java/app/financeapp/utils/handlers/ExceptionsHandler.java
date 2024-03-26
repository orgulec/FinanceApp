package app.financeapp.utils.handlers;

import app.financeapp.utils.exceptions.IncorrectBalanceValueException;
import app.financeapp.utils.exceptions.NoTransactionFoundedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(IncorrectBalanceValueException.class)
    public ResponseEntity<String> incorrectBalanceValueExceptionHandler(IncorrectBalanceValueException ex){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundExceptionHandler(EntityNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(NoTransactionFoundedException.class)
    public ResponseEntity<String> noTransactionFoundedExceptionHandler(NoTransactionFoundedException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
