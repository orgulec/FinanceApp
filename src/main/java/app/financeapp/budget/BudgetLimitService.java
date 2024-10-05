package app.financeapp.budget;

import app.financeapp.account.AccountModel;
import app.financeapp.account.AccountRepository;
import app.financeapp.dto.BudgetLimitDto;
import app.financeapp.dto.BudgetLimitResponseDto;
import app.financeapp.dto.TransactionRequestDto;
import app.financeapp.transaction.TransactionType;
import app.financeapp.utils.enums.ExceptionMsg;
import app.financeapp.utils.exceptions.AccountNotFoundException;
import app.financeapp.utils.exceptions.BudgetLimitAlreadyExistException;
import app.financeapp.utils.exceptions.BudgetNotFoundException;
import app.financeapp.utils.exceptions.IncorrectBalanceValueException;
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

    public List<BudgetLimitModel> getAllByAccountId(Long accountId) {
        List<BudgetLimitModel> allBudgetLimitList = budgetLimitRepository.findAllByAccount_Id(accountId);
        if (allBudgetLimitList.isEmpty()) {
            throw new BudgetNotFoundException(ExceptionMsg.NO_BUDGET_LIMIT_FOUNDED.toString());
        }
        return allBudgetLimitList;
    }

    public List<String> getStatisticsById(Long accountId) {
        List<BudgetLimitModel> budgetList = getAllByAccountId(accountId);

        return budgetList.stream()
                .map(this::showStatistics)
                .collect(Collectors.toList());
    }

    public BudgetLimitModel getByBudgetId(Long budgetId) {
        Optional<BudgetLimitModel> budgetOpt = budgetLimitRepository.findById(budgetId);
        if (budgetOpt.isEmpty()) {
            throw new BudgetNotFoundException(ExceptionMsg.BUDGET_LIMIT_NOT_FOUND.toString());
        }
        return budgetOpt.get();
    }

    String showStatistics(BudgetLimitModel model) {
        BudgetLimitResponseDto dto = new BudgetLimitResponseDto();
        dto.setTitle(model.getTitle());
        dto.setType(model.getType());
        dto.setUsedLimit(model.getUsedLimit());
        dto.setUpperLimit(model.getUpperLimit());

        return dto.showStats();
    }

    public BudgetLimitModel addNew(BudgetLimitDto dto) {
        AccountModel account = accountRepository.findById(dto.getAccount().getId()).orElseThrow(
                () -> new AccountNotFoundException(ExceptionMsg.NO_ACCOUNT_FOUNDED.toString()));
        BigDecimal budgetLimit = getLimitByAccountAndType(account.getId(), dto.getType())
                .getUpperLimit();
        if (!budgetLimit.equals(BigDecimal.ZERO)) {
            throw new BudgetLimitAlreadyExistException(ExceptionMsg.BUDGET_LIMIT_ALREADY_EXIST.toString());
        }
        BudgetLimitModel newBudget = new BudgetLimitModel();
        newBudget.setAccount(account);
        newBudget.setTitle(dto.getTitle());
        newBudget.setType(dto.getType());
        newBudget.setUpperLimit(dto.getLimit());
        newBudget.setUsedLimit(new BigDecimal(0));

        return budgetLimitRepository.save(newBudget);
    }

    public BudgetLimitModel getLimitByAccountAndType(Long accountId, TransactionType type) {
        List<BudgetLimitModel> budgetList = budgetLimitRepository.findAllByAccount_Id(accountId);
        Optional<BudgetLimitModel> budgetTypeLimit = budgetList.stream()
                .filter(b -> b.getType().equals(type))
                .findFirst();
        if (budgetTypeLimit.isEmpty()) {
            BudgetLimitModel limit = new BudgetLimitModel();
            limit.setUpperLimit(BigDecimal.ZERO);
            return limit;
        }
        return budgetTypeLimit.get();
    }

    public BudgetLimitModel addTransactionToBudgetLimit(TransactionRequestDto transaction, BudgetLimitModel budgetLimit) {
        BigDecimal sum = budgetLimit.getUsedLimit().add(transaction.getAmount());
        if (budgetLimit.getUpperLimit().compareTo(sum) < 0) {
            throw new IncorrectBalanceValueException(ExceptionMsg.AMOUNT_EXCEED_THE_BUDGET_LIMIT.toString());
        }
        budgetLimit.setUsedLimit(sum);
        return budgetLimitRepository.save(budgetLimit);

    }

    public void saveBudget(BudgetLimitModel newBudget) {
        budgetLimitRepository.save(newBudget);
    }
}
