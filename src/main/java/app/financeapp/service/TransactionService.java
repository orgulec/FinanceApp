package app.financeapp.service;

import app.financeapp.dto.TransactionDto;
import app.financeapp.dto.TransactionRequestDto;
import app.financeapp.model.AccountModel;
import app.financeapp.model.BudgetLimitModel;
import app.financeapp.model.TransactionModel;
import app.financeapp.model.enums.ExceptionMsg;
import app.financeapp.model.enums.TransactionType;
import app.financeapp.model.enums.TransactionsStatus;
import app.financeapp.repository.TransactionRepository;
import app.financeapp.utils.exceptions.IncorrectBalanceValueException;
import app.financeapp.utils.mappers.TransactionMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final BudgetLimitService budgetLimitService;
    private final TransactionMapper transactionMapper;


    public List<TransactionDto> getAllByAccountId(Long id) {
        AccountModel account = accountService.getById(id);
        List<TransactionModel> transactionsList = transactionRepository.findAllByAccount(account);
        if (transactionsList.isEmpty()) {
            throw new EntityNotFoundException(ExceptionMsg.NO_TRANSACTIONS_FOUNDED.toString());
        }
        return mapToDtoAndCollectToList(transactionsList);
    }

    public List<TransactionDto> getAllByAccountIdAndType(Long id, String type) {
        AccountModel account = accountService.getById(id);
        List<TransactionModel> transactionsList = transactionRepository.findAllByAccount(account)
                .stream()
                .filter(t -> t.getTransactionType().equals(TransactionType.valueOf(type)))
                .toList();
        if (transactionsList.isEmpty()) {
            throw new EntityNotFoundException(ExceptionMsg.NO_TRANSACTIONS_FOUNDED.toString());
        }
        return mapToDtoAndCollectToList(transactionsList);

    }

    @Transactional
    public TransactionModel makeTransfer(TransactionRequestDto transaction) {
        AccountModel fromAccount = accountService.getById(transaction.getFromAccount().getId());
        AccountModel toAccount = accountService.getById(transaction.getToAccount().getId());

        checkIfTransactionAndAccountsAreCorrect(transaction, fromAccount, toAccount);

        BudgetLimitModel limit = budgetLimitService.getLimitByAccountAndType(fromAccount.getId(), transaction.getTransactionType());
        if (!limit.getUpperLimit().equals(BigDecimal.valueOf(0))) {
            budgetLimitService.addTransactionToBudgetLimit(transaction, limit);
        }

        TransactionModel newTransaction = createTransactionFromDto(transaction, fromAccount, toAccount);

        accountService.subtractBalance(fromAccount.getId(), transaction.getAmount());
        accountService.addBalance(toAccount.getId(), transaction.getAmount());
        return transactionRepository.save(newTransaction);
    }

    private static TransactionModel createTransactionFromDto(TransactionRequestDto transaction, AccountModel fromAccount, AccountModel toAccount) {
        ZonedDateTime currentDate = ZonedDateTime.now();
        TransactionModel newTransaction = new TransactionModel();
            newTransaction.setTransactionType(transaction.getTransactionType());
            newTransaction.setAmount(transaction.getAmount());
            newTransaction.setFromAccount(fromAccount);
            newTransaction.setToAccount(toAccount);
            newTransaction.setTitle(transaction.getTitle());
            newTransaction.setTransactionDate(currentDate);
            newTransaction.setStatus(
                    checkIfCurrentDayIsWorkDay(currentDate) ? TransactionsStatus.ACCEPTED : TransactionsStatus.WAITING);
        return newTransaction;
    }

    private static void checkIfTransactionAndAccountsAreCorrect(TransactionRequestDto transaction, AccountModel fromAccount, AccountModel toAccount) {
        if (fromAccount == null || toAccount == null) {
            throw new EntityNotFoundException(ExceptionMsg.CHECK_ACCOUNTS_DATA.toString());
        }
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.valueOf(0)) < 0) {
            throw new IncorrectBalanceValueException(ExceptionMsg.INCORRECT_AMOUNT_VALUE.toString());
        }
        if (transaction.getAmount().compareTo(fromAccount.getBalance()) > 0) {
            throw new IncorrectBalanceValueException(ExceptionMsg.INSUFFICIENT_FUNDS_IN_ACCOUNT.toString());
        }
    }

    private List<TransactionDto> mapToDtoAndCollectToList(List<TransactionModel> transactionsList) {
        return transactionsList.stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

    private static boolean checkIfCurrentDayIsWorkDay(ZonedDateTime date){
        DayOfWeek currentDay = date.getDayOfWeek();
        return (!currentDay.equals(DayOfWeek.SATURDAY) && !currentDay.equals(DayOfWeek.SUNDAY));
    }

}
