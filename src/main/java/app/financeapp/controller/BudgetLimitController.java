package app.financeapp.controller;

import app.financeapp.dto.BudgetLimitDto;
import app.financeapp.model.BudgetLimitModel;
import app.financeapp.service.BudgetLimitService;
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
    @GetMapping("/allByAccount/{accountId}")
    public ResponseEntity<List<BudgetLimitModel>> getAllByAccountId(@PathVariable Long accountId){
        return ResponseEntity.ok(budgetLimitService.getAll(accountId));
    }

    /**
     * Gets all budget statistics
     * @param accountId id of the account
     * @return Strings with statistics from BudgetLimitResponseDto with status 200
     */
    @GetMapping("/statistics/{accountId}")
    public ResponseEntity<List<String>> getStatisticsByAccountId(@PathVariable Long accountId){
        return ResponseEntity.ok(budgetLimitService.getStatistics(accountId));
    }

    /**
     * Gets BudgetLimitModel by Id
     * @param budgetId id of the BudgetLimit
     * @return BudgetLimitModel with status 200
     */
    @GetMapping("/getById/{budgetId}")
    public ResponseEntity<BudgetLimitModel> getBudgetById(@PathVariable Long budgetId){
        return ResponseEntity.ok(budgetLimitService.getById(budgetId));
    }

    /**
     * Creates new BudgetLimitModel
     * @param dto Data to create new BudgetLimit
     * @return New BudgetLimitModel with status 201
     */
    @PostMapping("/createBudgetLimit")
    public ResponseEntity<BudgetLimitModel> createNewBudgetLimit(@RequestBody BudgetLimitDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetLimitService.addNew(dto));
    }
}
