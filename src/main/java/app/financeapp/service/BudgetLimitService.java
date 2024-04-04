package app.financeapp.service;

import app.financeapp.dto.BudgetLimitDto;
import app.financeapp.dto.BudgetLimitResponseDto;
import app.financeapp.dto.TransactionRequestDto;
import app.financeapp.model.AccountModel;
import app.financeapp.model.BudgetLimitModel;
import app.financeapp.model.enums.TransactionType;
import app.financeapp.repository.AccountRepository;
import app.financeapp.repository.BudgetLimitRepository;
import app.financeapp.utils.exceptions.BudgetLimitAlreadyExistException;
import app.financeapp.utils.exceptions.IncorrectBalanceValueException;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public String showStatistics(BudgetLimitModel model){
        BudgetLimitResponseDto dto = new BudgetLimitResponseDto();
        dto.setTitle(model.getTitle());
        dto.setType(model.getType());
        dto.setUsedLimit(model.getUsedLimit());
        dto.setUpperLimit(model.getUpperLimit());

        return dto.showStats();
    }

    public List<String> getStatistics(Long accountId) {
        List<BudgetLimitModel> budgetList = getAll(accountId);
        List<String> statistics = budgetList.stream()
                .map(this::showStatistics)
                .collect(Collectors.toList());
        return statistics;
    }

    public BudgetLimitModel addNew(BudgetLimitDto dto) {
        AccountModel account = accountRepository.findById(dto.getAccount().getId()).orElseThrow(() -> new EntityNotFoundException("No account founded."));
        BigDecimal budgetLimit = getLimitByAccountAndType(account.getId(), dto.getType()).getUpperLimit();
        if(!budgetLimit.equals(BigDecimal.ZERO)){
            throw new BudgetLimitAlreadyExistException("Budget limit already exist.");
        }
        BudgetLimitModel newBudget = new BudgetLimitModel();
            newBudget.setAccount(account);
            newBudget.setTitle(dto.getTitle());
            newBudget.setType(dto.getType());
            newBudget.setUpperLimit(dto.getLimit());
            newBudget.setUsedLimit(new BigDecimal(0));
        return budgetLimitRepository.save(newBudget);
    }

    public BudgetLimitModel getById(Long budgetId) {
        Optional<BudgetLimitModel> budgetOpt = budgetLimitRepository.findById(budgetId);
        if(budgetOpt.isEmpty()){
            throw new EntityNotFoundException("Budget Limit not found.");
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
            limit.setUpperLimit(BigDecimal.ZERO);
            return limit;
        }
        return budgetTypeLimit.get();
    }


    public void addTransactionToBudgetLimit(TransactionRequestDto transaction, BudgetLimitModel budgetLimit) {
        BigDecimal sum = budgetLimit.getUsedLimit().add(transaction.getAmount());
        if(budgetLimit.getUpperLimit().compareTo(sum)<0){
            throw new IncorrectBalanceValueException("The amount exceeds the budget limit");
        }
        else{
            budgetLimit.setUsedLimit(sum);
            budgetLimitRepository.save(budgetLimit);
        }
    }
}
