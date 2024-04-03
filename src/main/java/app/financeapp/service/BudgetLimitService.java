package app.financeapp.service;

import app.financeapp.dto.BudgetLimitDto;
import app.financeapp.dto.TransactionRequestDto;
import app.financeapp.model.AccountModel;
import app.financeapp.model.BudgetLimitModel;
import app.financeapp.model.enums.TransactionType;
import app.financeapp.repository.AccountRepository;
import app.financeapp.repository.BudgetLimitRepository;
import app.financeapp.utils.exceptions.IncorrectBalanceValueException;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class BudgetLimitService {

    private final BudgetLimitRepository budgetLimitRepository;
    private final AccountRepository accountRepository;

    public List<BudgetLimitModel> getAll(Long accountId) {
        List<BudgetLimitModel> allBudgetLimitList = budgetLimitRepository.findAllByAccount_Id(accountId);
        if(allBudgetLimitList.isEmpty()){
            throw new EntityNotFoundException("No budget limit founded.");
        }
        return allBudgetLimitList;
    }

    public BudgetLimitModel addNew(BudgetLimitDto dto) {
        AccountModel account = accountRepository.findById(dto.getAccount().getId()).orElseThrow(() -> new EntityNotFoundException("No account founded."));
        BudgetLimitModel newBudget = new BudgetLimitModel();
        newBudget.setTitle(dto.getTitle());
        newBudget.setAccount(account);
        newBudget.setType(dto.getType());
        newBudget.setLimit(dto.getLimit());

        return budgetLimitRepository.save(newBudget);
    }

    public BudgetLimitModel getById(Long budgetId) {
        Optional<BudgetLimitModel> budgetOpt = budgetLimitRepository.findById(budgetId);
        if(budgetOpt.isEmpty()){
            throw new EntityNotFoundException("Budget not found.");
        }
        return budgetOpt.get();
    }

    public BudgetLimitModel getLimitByAccountAndType(Long accountId, TransactionType type) {
        List<BudgetLimitModel> budgetList = budgetLimitRepository.findAllByAccount_Id(accountId);
        Optional<BudgetLimitModel> budgetTypeLimit = budgetList.stream()
                .filter(b -> b.getType().equals(type))
                .findFirst();

        if(budgetTypeLimit.isEmpty()){
            BudgetLimitModel limit = new BudgetLimitModel();
            limit.setLimit(BigDecimal.ZERO);
            return limit;
        }
        return budgetTypeLimit.get();
    }


    public void addTransactionToBudgetLimit(TransactionRequestDto transaction, BudgetLimitModel budgetLimit) {
        BigDecimal sum = budgetLimit.getUsedLimit().add(transaction.getAmount());
        if(budgetLimit.getLimit().compareTo(sum)<0){
            throw new IncorrectBalanceValueException("The amount exceeds the budget limit");
        }
        else{
            budgetLimit.setUsedLimit(sum);
            budgetLimitRepository.save(budgetLimit);
        }
    }
}
