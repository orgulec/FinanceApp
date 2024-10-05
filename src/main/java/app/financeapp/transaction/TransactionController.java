package app.financeapp.transaction;

import app.financeapp.dto.TransactionDto;
import app.financeapp.dto.TransactionRequestDto;
import app.financeapp.utils.exceptions.TransactionsNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Retrieves all transactions from DB
     * @return List of transactions wrapped in dto
     */
    @GetMapping("/all")
    public ResponseEntity<List<TransactionDto>> getAll(){
        List<TransactionDto> transactionsList = transactionService.getAll();
        if(transactionsList.isEmpty()){
            throw new TransactionsNotFoundException("No transactions founded.");
        }
        return ResponseEntity.ok(transactionsList);
    }


    /**
     * Gets all Transactions by account Id
     * @param id id of the Account
     * @return List of TransactionDtos and status 200
     */
    @Operation(summary = "Gets all Transactions by account Id", description = "Returns List of TransactionDtos and status 200")
    @GetMapping("/{id}")
    public ResponseEntity<List<TransactionDto>> getAllTransactionsByAccountId(@PathVariable @NotNull Long id){
        return ResponseEntity.ok(transactionService.getAllByAccountId(id));
    }

    /**
     * Gets all Transactions by account Id and TransactionType
     * @param id id of the Account
     * @param type Type of Transaction
     * @return List of TransactionDtos and status 200
     */
    @Operation(summary = "Gets all Transactions by account Id and TransactionType", description = "Returns List of TransactionDtos and status 200")
    @GetMapping("/")
    public ResponseEntity<List<TransactionDto>> getAllTransactionsByAccountIdAndType(@NotNull @RequestParam(name = "id") Long id, @RequestParam(name = "type") String type){
        return ResponseEntity.ok(transactionService.getAllByAccountIdAndType(id, type));
    }

    /**
     * Make the new transaction between two accounts
     * @param transaction data required to make transaction between 2 accounts
     * @return new TransactionModel with status 202
     */
    @Operation(summary = "Make the new transaction between two accounts", description = "Returns new TransactionModel with status 202")
    @PutMapping("/payment")
    public ResponseEntity<TransactionModel> makeTransaction(@Valid @RequestBody TransactionRequestDto transaction){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(transactionService.makeTransfer(transaction));
    }

}
