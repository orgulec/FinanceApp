package app.financeapp.service;

import app.financeapp.dto.TransactionDto;
import app.financeapp.dto.TransactionRequestDto;
import app.financeapp.model.AccountModel;
import app.financeapp.model.DepositModel;
import app.financeapp.model.TransactionModel;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final TransactionMapper transactionMapper;


    public List<TransactionDto> getAllByAccountId(Long id) {
        AccountModel account = accountService.getById(id);
        List<TransactionModel> transactionsList = transactionRepository.findAllByAccount(account);
        if(transactionsList.isEmpty()){
            throw new EntityNotFoundException("No transactions founded.");
        }
        return transactionsList.stream().map(transactionMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public TransactionModel makeTransfer(TransactionRequestDto transaction) {
        AccountModel fromAccount = accountService.getById(transaction.getFromAccount().getId());
        AccountModel toAccount = accountService.getById(transaction.getToAccount().getId());

        checkIfTransactionAndAccountsAreCorrect(transaction, fromAccount, toAccount);

        TransactionModel newTransaction = createTransactionFromDto(transaction, fromAccount, toAccount);

        accountService.substractBalance(fromAccount.getId(), transaction.getAmount());
        accountService.addBalance(toAccount.getId(), transaction.getAmount());
        return transactionRepository.save(newTransaction);
    }

    private static void checkIfTransactionAndAccountsAreCorrect(TransactionRequestDto transaction, AccountModel fromAccount, AccountModel toAccount) {
        if(fromAccount == null || toAccount == null){
            throw new EntityNotFoundException("Check accounts data!");
        }
        if(transaction.getAmount() == null || transaction.getAmount().compareTo(new BigDecimal("0")) < 0){
            throw new IncorrectBalanceValueException("Incorrect amount value.");
        }
        if(transaction.getAmount().compareTo(fromAccount.getBalance()) > 0){
            throw new IncorrectBalanceValueException("Insufficient funds in the account.");
        }
    }

    private static TransactionModel createTransactionFromDto(TransactionRequestDto transaction, AccountModel fromAccount, AccountModel toAccount) {
        TransactionModel newTransaction = new TransactionModel();
        newTransaction.setTransactionType(transaction.getTransactionType());
        newTransaction.setAmount(transaction.getAmount());
        newTransaction.setFromAccount(fromAccount);
        newTransaction.setToAccount(toAccount);
        newTransaction.setTitle(transaction.getTitle());
        newTransaction.setStatus(TransactionsStatus.WAITING);
        return newTransaction;
    }


}
