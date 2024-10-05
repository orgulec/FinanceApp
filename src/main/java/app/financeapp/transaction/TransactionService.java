package app.financeapp.transaction;

import app.financeapp.account.AccountModel;
import app.financeapp.account.AccountService;
import app.financeapp.budget.BudgetLimitModel;
import app.financeapp.budget.BudgetLimitService;
import app.financeapp.deposit.DepositModel;
import app.financeapp.deposit.DepositService;
import app.financeapp.dto.DepositDto;
import app.financeapp.dto.TransactionDto;
import app.financeapp.dto.TransactionRequestDto;
import app.financeapp.utils.enums.ExceptionMsg;
import app.financeapp.utils.exceptions.IncorrectBalanceValueException;
import app.financeapp.utils.exceptions.TransactionsNotFoundException;
import app.financeapp.utils.mappers.DepositMapper;
import app.financeapp.utils.mappers.TransactionMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
    private final DepositService depositService;

    private final TransactionMapper transactionMapper;
    private final DepositMapper depositMapper;


    public TransactionModel saveTransaction(TransactionModel newTransaction){
        return transactionRepository.save(newTransaction);
    }
    public List<TransactionDto> getAll(){
        List<TransactionModel> transactions = transactionRepository.findAll();
        if(transactions.isEmpty()) {
            throw new TransactionsNotFoundException(ExceptionMsg.NO_TRANSACTIONS_FOUNDED.toString());
        }
        return transactions.stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

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

    public void transferToDeposit(DepositModel newDeposit, AccountModel fromAccount) {
        if(fromAccount.getBalance().compareTo(newDeposit.getBalance())<0){
            throw new IncorrectBalanceValueException(ExceptionMsg.INSUFFICIENT_FUNDS_IN_ACCOUNT.toString());
        }
        String title = "Own deposit transfer.";
        TransactionModel transaction = new TransactionModel();

        transaction.setTransactionType(TransactionType.SAVINGS);
        transaction.setTitle(title);
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(fromAccount);
        transaction.setAmount(newDeposit.getBalance());
        transaction.setStatus(TransactionsStatus.ACCEPTED);

        accountService.subtractBalance(fromAccount.getId(), newDeposit.getBalance());
        transactionRepository.save(transaction);
    }

    @Transactional
    public DepositModel addNewDepositToUser(@Valid DepositDto deposit) {
        AccountModel account = accountService.getById(deposit.getAccountId());
        DepositModel newDeposit = depositMapper.toModel(deposit);
        newDeposit.setAccount(account);

        transferToDeposit(newDeposit, account);
        return depositService.saveDeposit(newDeposit);
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
