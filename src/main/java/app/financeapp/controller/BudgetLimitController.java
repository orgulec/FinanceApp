package app.financeapp.controller;

import app.financeapp.dto.BudgetLimitDto;
import app.financeapp.model.BudgetLimitModel;
import app.financeapp.service.BudgetLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/budget")
public class BudgetLimitController {

    private final BudgetLimitService budgetLimitService;

    @GetMapping("/allByAccount/{accountId}")
    public ResponseEntity<List<BudgetLimitModel>> getAllByAccountId(@PathVariable Long accountId){
        return ResponseEntity.ok(budgetLimitService.getAll(accountId));
    }

    @GetMapping("/statistics/{accountId}")
    public ResponseEntity<List<String>> getStatisticsByAccountId(@PathVariable Long accountId){
        return ResponseEntity.ok(budgetLimitService.getStatistics(accountId));
    }

    @GetMapping("/getByType/{budgetId}")
    public ResponseEntity<BudgetLimitModel> getBudgetByIdAndType(@PathVariable Long budgetId){
        return ResponseEntity.ok(budgetLimitService.getById(budgetId));
    }

    @PostMapping("/createBudgetLimit")
    public ResponseEntity<BudgetLimitModel> createNewBudgetLimit(@RequestBody BudgetLimitDto dto){
        return ResponseEntity.ok(budgetLimitService.addNew(dto));
    }
}
