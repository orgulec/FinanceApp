package app.financeapp.service;

import app.financeapp.dto.TransactionDto;
import app.financeapp.dto.TransactionRequestDto;
import app.financeapp.model.AccountModel;
import app.financeapp.model.BudgetLimitModel;
import app.financeapp.model.TransactionModel;
import app.financeapp.model.enums.AccountType;
import app.financeapp.model.enums.TransactionType;
import app.financeapp.model.enums.TransactionsStatus;
import app.financeapp.repository.TransactionRepository;
import app.financeapp.utils.mappers.TransactionMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
@Mock
private TransactionRepository transactionRepository;
@Mock
private AccountService accountService;
@Mock
private TransactionMapper transactionMapper;
@Mock
private BudgetLimitService budgetLimitService;
@InjectMocks
private TransactionService transactionService;

    @Test
    void getAllByAccountId_shouldReturnTransactionDtoList() {
        //given
        Long id = 1L;
        AccountModel account = new AccountModel();
            account.setAccountNumber("1234567890");
            account.setLogin("login");
            account.setPassword("pass");
            account.setBalance(new BigDecimal(100));
            account.setType(AccountType.CASH);
        TransactionModel transaction = new TransactionModel();
            transaction.setAmount(new BigDecimal(10));
            transaction.setTransactionDate(ZonedDateTime.now());
            transaction.setFromAccount(account);
            transaction.setToAccount(new AccountModel());
        List<TransactionModel> transactionList = new ArrayList<>();
            transactionList.add(transaction);
        //when
        when(accountService.getById(id)).thenReturn(account);
        when(transactionRepository.findAllByAccount(account)).thenReturn(transactionList);
        when(transactionMapper.toDto(transaction)).thenCallRealMethod();
        //then
        List<TransactionDto> result = transactionService.getAllByAccountId(id);
        assertAll(
                ()->assertNotNull(result),
                ()->assertEquals(1, result.size()),
                ()->assertEquals(transactionList.get(0).getFromAccount(), result.get(0).getFromAccount())
        );
    }

    @Test
    void getAllByAccountId_shouldThrowEntityNotFoundException() {
        //given
        Long id = 1L;
        AccountModel account = new AccountModel();
            account.setAccountNumber("1234567890");
            account.setLogin("login");
            account.setPassword("pass");
            account.setBalance(new BigDecimal(100));
            account.setType(AccountType.CASH);
        List<TransactionModel> transactionList = new ArrayList<>();
        //when
        when(accountService.getById(id)).thenReturn(account);
        when(transactionRepository.findAllByAccount(account)).thenReturn(transactionList);
        //then
        assertThrows(EntityNotFoundException.class, ()->transactionService.getAllByAccountId(id));
    }

    @Test
    void getAllByAccountIdAndType_shouldReturnTransactionDtoList() {
        //given
        Long id = 1L;
        String type = "OTHERS";
        AccountModel account = new AccountModel();
            account.setAccountNumber("1234567890");
            account.setLogin("login");
            account.setPassword("pass");
            account.setBalance(new BigDecimal(100));
            account.setType(AccountType.CASH);
        TransactionModel transaction = new TransactionModel();
            transaction.setAmount(new BigDecimal(10));
            transaction.setTransactionDate(ZonedDateTime.now());
            transaction.setFromAccount(account);
            transaction.setToAccount(new AccountModel());
            transaction.setTransactionType(TransactionType.OTHERS);
        List<TransactionModel> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        //when
        when(accountService.getById(id)).thenReturn(account);
        when(transactionRepository.findAllByAccount(account)).thenReturn(transactionList);
        when(transactionMapper.toDto(transaction)).thenCallRealMethod();
        //then
        List<TransactionDto> result = transactionService.getAllByAccountIdAndType(id, type);
        assertAll(
                ()->assertNotNull(result),
                ()->assertEquals(1, result.size()),
                ()->assertEquals(transactionList.get(0).getFromAccount(), result.get(0).getFromAccount()),
                ()->assertEquals(transactionList.get(0).getTransactionType(), result.get(0).getTransactionType())
        );
    }

    @Test
    void makeTransfer_shouldReturnNewTransactionModel() {
        //given
        AccountModel fromAccount = new AccountModel();
            fromAccount.setBalance(new BigDecimal(100));
        AccountModel toAccount = new AccountModel();
            toAccount.setBalance(new BigDecimal(10));
        TransactionRequestDto transactionDto = new TransactionRequestDto();
            transactionDto.setTitle("Tytuł");
            transactionDto.setAmount(new BigDecimal(10));
            transactionDto.setTransactionType(TransactionType.OTHERS);
            transactionDto.setFromAccount(fromAccount);
            transactionDto.setToAccount(toAccount);
        TransactionModel newTransaction = new TransactionModel();
            newTransaction.setTitle("Tytuł");
            newTransaction.setAmount(new BigDecimal(10));
            newTransaction.setTransactionType(TransactionType.OTHERS);
            newTransaction.setFromAccount(fromAccount);
            newTransaction.setToAccount(toAccount);
        BudgetLimitModel budget = new BudgetLimitModel();
            budget.setUpperLimit(new BigDecimal(0));
            budget.setType(TransactionType.OTHERS);

        //when
        when(accountService.getById(fromAccount.getId())).thenReturn(fromAccount);
        when(accountService.getById(toAccount.getId())).thenReturn(toAccount);
        when(budgetLimitService.getLimitByAccountAndType(fromAccount.getId(),TransactionType.OTHERS)).thenReturn(budget);
        doNothing().when(accountService).subtractBalance(fromAccount.getId(), transactionDto.getAmount());
        doNothing().when(accountService).addBalance(toAccount.getId(), transactionDto.getAmount());
        when(transactionRepository.save(any(TransactionModel.class))).thenReturn(newTransaction);

        //then
        TransactionModel result = transactionService.makeTransfer(transactionDto);
        verify(transactionRepository).save(any(TransactionModel.class));
        assertAll(
                ()->assertNotNull(result),
                ()->assertEquals(newTransaction.getTitle(), result.getTitle())
        );
    }
}