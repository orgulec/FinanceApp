package app.financeapp.service;

import app.financeapp.dto.TransactionRequestDto;
import app.financeapp.model.AccountModel;
import app.financeapp.model.TransactionModel;
import app.financeapp.model.enums.TransactionsStatus;
import app.financeapp.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Data
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    @Transactional
    public TransactionModel makeTransfer(TransactionRequestDto transaction) {
        AccountModel fromAccount = accountService.getById(transaction.getFromAccount().getId());
        AccountModel toAccount = accountService.getById(transaction.getToAccount().getId());

        if(fromAccount == null || toAccount == null){
            throw new EntityNotFoundException("Check accounts data!");
        }
        if(transaction.getAmount() == null || transaction.getAmount().compareTo(new BigDecimal("0")) < 0){
            throw new IllegalArgumentException("Incorrect amount value.");
        }
        if(transaction.getAmount().compareTo(fromAccount.getBalance()) > 0){
            throw new IllegalArgumentException("Insufficient funds in the account.");
        }

        TransactionModel newTransaction = new TransactionModel();
        newTransaction.setTransactionType(transaction.getTransactionType());
        newTransaction.setAmount(transaction.getAmount());
        newTransaction.setFromAccount(fromAccount);
        newTransaction.setToAccount(toAccount);
        newTransaction.setTitle(transaction.getTitle());
        newTransaction.setStatus(TransactionsStatus.WAITING);

        accountService.substractBalance(fromAccount.getId(), transaction.getAmount());
        accountService.addBalance(toAccount.getId(), transaction.getAmount());
        return transactionRepository.save(newTransaction);

    }

    public List<TransactionModel> getAllByAccountId(Long id) {
        AccountModel account = accountService.getById(id);
        List<TransactionModel> transactionsList = transactionRepository.findAllByAccount(account);
        if(transactionsList.isEmpty()){
            throw new RuntimeException("No transactions founded.");
        }
        return transactionsList;
    }
}
