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

    @GetMapping("/{id}")
    public ResponseEntity<AccountRequestDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(accountService.getByIdDto(id));
    }
    @GetMapping("/userAccounts")
    public ResponseEntity<List<AccountRequestDto>> getAllByUser(@Valid @RequestBody UserDto user){
        return ResponseEntity.ok(accountService.getAllByUser(user));
    }

    @GetMapping("/historyById/{id}")
    public ResponseEntity<List<TransactionDto>> getAllByAccountId(@PathVariable Long id){
        return ResponseEntity.ok(transactionService.getAllByAccountId(id));
    }

    @PutMapping("/payment")
    public ResponseEntity<TransactionModel> makeTransaction(@Valid @RequestBody TransactionRequestDto transaction){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(transactionService.makeTransfer(transaction));
    }

    @PostMapping("/createAccount")
    public ResponseEntity<AccountModel> createNewAccountForUser(@Valid @RequestBody AccountNewDto newAccount){
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.addNewAccountToUser(newAccount));
    }

    @PostMapping("/createDeposit")
    public ResponseEntity<DepositModel> createNewDepositForUser(@Valid @RequestBody DepositDto deposit){
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.addNewDepositToUser(deposit));
    }

}
