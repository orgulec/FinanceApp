package app.financeapp.controller;

import app.financeapp.dto.AccountNewDto;
import app.financeapp.dto.AccountRequestDto;
import app.financeapp.dto.LoginDto;
import app.financeapp.dto.TransactionRequestDto;
import app.financeapp.model.AccountModel;
import app.financeapp.model.TransactionModel;
import app.financeapp.service.AccountService;
import app.financeapp.service.TransactionService;
import app.financeapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<AccountRequestDto>> getAllByUser(@Valid @RequestBody LoginDto login){
        return ResponseEntity.ok(accountService.getAllByUser(login));
    }

    @GetMapping("/historyById/{id}")
    public ResponseEntity<List<TransactionModel>> getAllByAccountId(@PathVariable Long id){
        return ResponseEntity.ok(transactionService.getAllByAccountId(id));
    }

    @PutMapping("/payment")
    public ResponseEntity<TransactionModel> makeTransaction(@Valid @RequestBody TransactionRequestDto transaction){
        return ResponseEntity.ok(transactionService.makeTransfer(transaction));
    }

    @PostMapping("/createAccount")
    public ResponseEntity<AccountModel> createAccountForUser(@Valid @RequestBody AccountNewDto newDto){
        return ResponseEntity.ok(accountService.addNewAccountToUser(newDto));
    }

}
