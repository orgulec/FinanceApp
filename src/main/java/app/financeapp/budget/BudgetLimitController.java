package app.financeapp.budget;

import app.financeapp.dto.BudgetLimitDto;
import app.financeapp.utils.exceptions.AccountNotFoundException;
import app.financeapp.utils.exceptions.BudgetLimitAlreadyExistException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/budget")
public class BudgetLimitController {

    private final BudgetLimitService budgetLimitService;

    /**
     * Gets all budgets by an account id
     * @param accountId id of the account
     * @return List of BudgetLimitModels with status 200
     */
    @GetMapping("/byAccount/{accountId}")
    public ResponseEntity<List<BudgetLimitModel>> getAllByAccountId(@PathVariable @NotNull Long accountId){
        return ResponseEntity.ok(budgetLimitService.getAllByAccountId(accountId));
    }

    /**
     * Gets BudgetLimitModel by Id
     * @param budgetId id of the BudgetLimit
     * @return BudgetLimitModel with status 200
     */
    @GetMapping("/byId/{budgetId}")
    public ResponseEntity<BudgetLimitModel> getBudgetById(@PathVariable @NotNull Long budgetId){
        return ResponseEntity.ok(budgetLimitService.getByBudgetId(budgetId));
    }

    /**
     * Gets string with budget statistics
     * @param accountId id of the account
     * @return Strings with statistics from BudgetLimitResponseDto with status 200
     */
    @GetMapping("/statistics/{accountId}")
    public ResponseEntity<List<String>> getStatisticsByAccountId(@PathVariable @NotNull Long accountId){
        return ResponseEntity.ok(budgetLimitService.getStatisticsById(accountId));
    }

    /**
     * Creates new BudgetLimitModel
     * @param dto Data to create new BudgetLimit
     * @return New BudgetLimitModel with status 201
     */
    @PostMapping("/new")
    public ResponseEntity<BudgetLimitModel> createNewBudgetLimit(@RequestBody @Validated BudgetLimitDto dto){
/*        try {
            BudgetLimitModel result = budgetLimitService.addNew(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (BudgetLimitAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (AccountNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }*/
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetLimitService.addNew(dto));
    }
}
