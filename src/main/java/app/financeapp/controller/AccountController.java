package app.financeapp.controller;

import app.financeapp.dto.*;
import app.financeapp.model.AccountModel;
import app.financeapp.model.DepositModel;
import app.financeapp.model.TransactionModel;
import app.financeapp.service.AccountService;
import app.financeapp.service.TransactionService;
import app.financeapp.service.UserService;
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
    private final UserService userService;

    /**
     * Gets account by Id
     * @param id Id of the account
     * @return AccountDto with status 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountRequestDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(accountService.getByIdDto(id));
    }

    /**
     * Gets all accounts belonging to the user
     * @param user owner of accounts we are looking for
     * @return List of AccountDtos and status 200
     */
    @GetMapping("/userAccounts")
    public ResponseEntity<List<AccountRequestDto>> getAllByUser(@Valid @RequestBody UserDto user){
        return ResponseEntity.ok(accountService.getAllByUser(user));
    }

    /**
     * Gets all Transactions by account Id
     * @param id Id of the Account
     * @return List of TransactionDtos and status 200
     */
    @GetMapping("/historyById/{id}")
    public ResponseEntity<List<TransactionModel>> getAllByAccountId(@PathVariable Long id){
        return ResponseEntity.ok(transactionService.getAllByAccountId(id));
    }

    /**
     * Make the new transaction between two accounts
     * @param transaction data required to make transaction between 2 accounts
     * @return new TransactionModel with status 202
     */
    @PutMapping("/payment")
    public ResponseEntity<TransactionModel> makeTransaction(@Valid @RequestBody TransactionRequestDto transaction){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(transactionService.makeTransfer(transaction));
    }

    /**
     * Creates a new account
     * @param newAccount Data to create new account
     * @return New AccountModel with status 201
     */
    @PostMapping("/createAccount")
    public ResponseEntity<AccountModel> createNewAccountForUser(@Valid @RequestBody AccountNewDto newAccount){
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.addNewAccountToUser(newAccount));
    }

    /**
     * Creates a new depoposit on the user account
     * @param deposit Data to create new deposit
     * @return New DepositModel with status 201
     */
    @PostMapping("/createDeposit")
    public ResponseEntity<DepositModel> createNewDepositForUser(@Valid @RequestBody DepositDto deposit){
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.addNewDepositToUser(deposit));
    }

}
