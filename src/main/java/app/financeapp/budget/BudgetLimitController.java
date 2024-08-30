package app.financeapp.budget;

import app.financeapp.dto.BudgetLimitDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<BudgetLimitModel>> getAllByAccountId(@NotNull @PathVariable Long accountId){
        return ResponseEntity.ok(budgetLimitService.getAll(accountId));
    }

    /**
     * Gets BudgetLimitModel by Id
     * @param budgetId id of the BudgetLimit
     * @return BudgetLimitModel with status 200
     */
    @GetMapping("/byId/{budgetId}")
    public ResponseEntity<BudgetLimitModel> getBudgetById(@NotNull @PathVariable Long budgetId){
        return ResponseEntity.ok(budgetLimitService.getById(budgetId));
    }

    /**
     * Gets all budget statistics
     * @param accountId id of the account
     * @return Strings with statistics from BudgetLimitResponseDto with status 200
     */
    @GetMapping("/statistics/{accountId}")
    public ResponseEntity<List<String>> getStatisticsByAccountId(@NotNull @PathVariable Long accountId){
        return ResponseEntity.ok(budgetLimitService.getStatistics(accountId));
    }

    /**
     * Creates new BudgetLimitModel
     * @param dto Data to create new BudgetLimit
     * @return New BudgetLimitModel with status 201
     */
    @PostMapping("/newBudgetLimit")
    public ResponseEntity<BudgetLimitModel> createNewBudgetLimit(@Valid @RequestBody BudgetLimitDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetLimitService.addNew(dto));
    }
}
