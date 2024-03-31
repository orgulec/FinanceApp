package app.financeapp.controller;

import app.financeapp.dto.*;
import app.financeapp.model.AccountModel;
import app.financeapp.model.DepositModel;
import app.financeapp.model.TransactionModel;
import app.financeapp.service.AccountService;
import app.financeapp.service.TransactionService;
import app.financeapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    private final TransactionService transactionService;

    /**
     * Gets an account by Id
     * @param id Id of the account
     * @return AccountDto with status 200
     */
    @Operation(summary = "Gets an account by Id", description = "Returns AccountDto with status 200")
    @GetMapping("/{id}")
    public ResponseEntity<AccountRequestDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(accountService.getByIdDto(id));
    }

    /**
     * Gets all accounts belonging to the user
     * @param user owner of accounts we are looking for
     * @return List of AccountDtos and status 200
     */
    @Operation(summary = "Gets all accounts belonging to the user", description = "Returns List of AccountDtos and status 200")
    @GetMapping("/userAccounts")
    public ResponseEntity<List<AccountRequestDto>> getAllByUser(@Valid @RequestBody UserDto user){
        return ResponseEntity.ok(accountService.getAllByUser(user));
    }

    /**
     * Gets all Transactions by account Id
     * @param id Id of the Account
     * @return List of TransactionDtos and status 200
     */
    @Operation(summary = "Gets all Transactions by account Id", description = "Returns List of TransactionDtos and status 200")
    @GetMapping("/historyById/{id}")
    public ResponseEntity<List<TransactionDto>> getAllTransactionsByAccountId(@PathVariable Long id){
        return ResponseEntity.ok(transactionService.getAllByAccountId(id));
    }

    /**
     * Gets all Transactions by account Id and TransactionType
     * @param id Id of the Account
     * @param type Type of Transaction
     * @return List of TransactionDtos and status 200
     */
    @Operation(summary = "Gets all Transactions by account Id and TransactionType", description = "Returns List of TransactionDtos and status 200")
    @GetMapping("/historyByType")
    public ResponseEntity<List<TransactionDto>> getAllTransactionsByAccountIdAndType(@RequestParam(name = "id") Long id, @RequestParam(name = "type") String type){
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

    /**
     * Creates a new account
     * @param newAccount Data to create new account
     * @return New AccountModel with status 201
     */
    @Operation(summary = "Creates a new account", description = "Returns new AccountModel with status 201")
    @PostMapping("/createAccount")
    public ResponseEntity<AccountModel> createNewAccountForUser(@Valid @RequestBody AccountNewDto newAccount){
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.addNewAccountToUser(newAccount));
    }

    /**
     * Creates a new deposit on the user account
     * @param deposit Data to create new deposit
     * @return New DepositModel with status 201
     */
    @Operation(summary = "Creates a new deposit on the user account", description = "Returns new DepositModel with status 201")
    @PostMapping("/createDeposit")
    public ResponseEntity<DepositModel> createNewDepositForUser(@Valid @RequestBody DepositDto deposit){
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.addNewDepositToUser(deposit));
    }

}
