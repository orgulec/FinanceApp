package app.financeapp.transaction;

import app.financeapp.utils.exceptions.NoTransactionFoundException;
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

    /**
     * Retrieves all transactions from DB
     * @return List of transactions wrapped in dto
     */
    @GetMapping("/all")
    public ResponseEntity<List<TransactionModel>> getAll(){
        List<TransactionModel> transactionsList = transactionRepository.findAll();
        if(transactionsList.isEmpty()){
            throw new NoTransactionFoundException("No transactions founded.");
        }
        return ResponseEntity.ok(transactionsList);
    }

}
