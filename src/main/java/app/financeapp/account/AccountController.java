package app.financeapp.account;

import app.financeapp.deposit.DepositModel;
import app.financeapp.dto.AccountNewDto;
import app.financeapp.dto.AccountRequestDto;
import app.financeapp.dto.DepositDto;
import app.financeapp.dto.UserDto;
import app.financeapp.transaction.TransactionService;
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
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    /**
     * Gets an account by id
     * @param id id of the account
     * @return AccountDto with status 200
     */
    @Operation(summary = "Gets an account by Id", description = "Returns AccountDto with status 200")
    @GetMapping("/{id}")
    public ResponseEntity<AccountRequestDto> getById(@PathVariable @NotNull Long id){
        return ResponseEntity.ok(accountService.getByIdDto(id));
    }

    /**
     * Gets all accounts belonging to the user
     * @param user owner of accounts we are looking for
     * @return List of AccountDtos and status 200
     */
    @Operation(summary = "Gets all accounts belonging to the user", description = "Returns List of AccountDtos and status 200")
    @GetMapping("/")
    public ResponseEntity<List<AccountRequestDto>> getAllByUser(@Valid @RequestBody UserDto user){
        return ResponseEntity.ok(accountService.getAllByUser(user));
    }

    /**
     * Creates a new account
     * @param newAccount Data to create new account
     * @return New AccountModel with status 201
     */
    @Operation(summary = "Creates a new account", description = "Returns new AccountModel with status 201")
    @PostMapping("/account")
    public ResponseEntity<AccountModel> createNewAccountForUser(@Valid @RequestBody AccountNewDto newAccount){
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.addNewAccountToUser(newAccount));
    }

    /**
     * Creates a new deposit on the user account
     * @param deposit Data to create new deposit
     * @return New DepositModel with status 201
     */
    @Operation(summary = "Creates a new deposit on the user account", description = "Returns new DepositModel with status 201")
    @PostMapping("/deposit")
    public ResponseEntity<DepositModel> createNewDepositForUser(@Valid @RequestBody DepositDto deposit){
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.addNewDepositToUser(deposit));
    }

}
