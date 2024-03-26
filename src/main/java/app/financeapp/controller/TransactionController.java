package app.financeapp.controller;

import app.financeapp.model.TransactionModel;
import app.financeapp.repository.TransactionRepository;
import app.financeapp.utils.exceptions.NoTransactionFoundedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final AccountController accountController;

    /**
     * Retives all transactions from DB
     * @return List of transactions wrapped in dto
     */
    @GetMapping("/all")
    public ResponseEntity<List<TransactionModel>> getAll(){
        List<TransactionModel> transactionsList = transactionRepository.findAll();
        if(transactionsList.isEmpty()){
            throw new NoTransactionFoundedException("No transactions founded.");
        }
        return ResponseEntity.ok(transactionsList);
    }

}
