package app.financeapp.utils.handlers;

import app.financeapp.utils.exceptions.BudgetLimitAlreadyExistException;
import app.financeapp.utils.exceptions.IncorrectBalanceValueException;
import app.financeapp.utils.exceptions.NoTransactionFoundException;
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
    @ExceptionHandler(NoTransactionFoundException.class)
    public ResponseEntity<String> noTransactionFoundedExceptionHandler(NoTransactionFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(BudgetLimitAlreadyExistException.class)
    public ResponseEntity<String> budgetLimitAlreadyExistExceptionHandler(BudgetLimitAlreadyExistException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}
