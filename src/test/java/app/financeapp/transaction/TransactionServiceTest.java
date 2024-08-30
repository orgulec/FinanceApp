package app.financeapp.transaction;

import app.financeapp.account.AccountModel;
import app.financeapp.account.AccountService;
import app.financeapp.account.AccountType;
import app.financeapp.budget.BudgetLimitModel;
import app.financeapp.budget.BudgetLimitService;
import app.financeapp.deposit.DepositModel;
import app.financeapp.deposit.DepositService;
import app.financeapp.dto.DepositDto;
import app.financeapp.dto.TransactionDto;
import app.financeapp.dto.TransactionRequestDto;
import app.financeapp.utils.exceptions.IncorrectBalanceValueException;
import app.financeapp.utils.mappers.DepositMapper;
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
import java.util.Optional;

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
    private BudgetLimitService budgetLimitService;
    @Mock
    private DepositService depositService;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private DepositMapper depositMapper;
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
        when(transactionMapper.toDto(any(TransactionModel.class))).thenCallRealMethod();
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
        //when
        when(accountService.getById(id)).thenReturn(account);
        when(transactionRepository.findAllByAccount(account)).thenThrow(new EntityNotFoundException());
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
        when(transactionMapper.toDto(any(TransactionModel.class))).thenCallRealMethod();
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
        when(transactionService.saveTransaction(any(TransactionModel.class))).thenReturn(newTransaction);

        //then
        TransactionModel result = transactionService.makeTransfer(transactionDto);
        verify(transactionRepository).save(any(TransactionModel.class));
        assertAll(
                ()->assertNotNull(result),
                ()->assertEquals(newTransaction.getTitle(), result.getTitle())
        );
    }

    @Test
    void addNewDepositToUser_shouldReturnNewDepositModel() {
        //given
        AccountModel account =  new AccountModel();
        account.setBalance(new BigDecimal(10));
        DepositDto depositDto = new DepositDto();
        depositDto.setAccountId(account.getId());
        depositDto.setBalance(new BigDecimal(0));
        depositDto.setPlannedEndDate(ZonedDateTime.now());
        DepositModel newDeposit = new DepositModel();
        newDeposit.setAccount(account);
        newDeposit.setBalance(new BigDecimal(0));
        newDeposit.setPlannedEndDate(ZonedDateTime.now());
        //when
        when(accountService.getById(depositDto.getAccountId())).thenReturn(account);
        when(depositMapper.toModel(depositDto)).thenReturn(newDeposit);
        when(depositService.saveDeposit(newDeposit)).thenReturn(newDeposit);
        //then
        DepositModel result = transactionService.addNewDepositToUser(depositDto);
        assertAll(
                ()->assertNotNull(result),
                ()->assertEquals(newDeposit.getAccount(), result.getAccount()),
                ()->assertEquals(newDeposit.getBalance(), result.getBalance())
        );
    }

    @Test
    void transferToDeposit_shouldSaveNewTransaction() {
        //given
        AccountModel fromAccount = new AccountModel();
        fromAccount.setAccountNumber("123456789");
        fromAccount.setLogin("login");
        fromAccount.setPassword("pass");
        fromAccount.setBalance(new BigDecimal(100));
        fromAccount.setType(AccountType.CASH);
        DepositModel newDeposit = new DepositModel();
        newDeposit.setBalance(new BigDecimal(50));
        newDeposit.setCreationDate(ZonedDateTime.now());
        newDeposit.setPlannedEndDate(ZonedDateTime.now());
        TransactionModel transaction = new TransactionModel();
        transaction.setToAccount(fromAccount);
        transaction.setTransactionDate(ZonedDateTime.now());
        //when
        when(transactionRepository.save(any(TransactionModel.class))).thenReturn(transaction);
        //then
        transactionService.transferToDeposit(newDeposit, fromAccount);
        verify(transactionRepository).save(any(TransactionModel.class));
    }
    @Test
    void transferToDeposit_shouldThrowIncorrectBalanceValueExceptionWhenTooLessCashOnAccount() {
        //given
        AccountModel fromAccount = new AccountModel();
        fromAccount.setAccountNumber("123456789");
        fromAccount.setLogin("login");
        fromAccount.setPassword("pass");
        fromAccount.setBalance(new BigDecimal(0));
        fromAccount.setType(AccountType.CASH);
        DepositModel newDeposit = new DepositModel();
        newDeposit.setBalance(new BigDecimal(50));
        newDeposit.setCreationDate(ZonedDateTime.now());
        newDeposit.setPlannedEndDate(ZonedDateTime.now());

        //when
        //then
        assertThrows(IncorrectBalanceValueException.class,
                () -> transactionService.transferToDeposit(newDeposit, fromAccount));
        verify(transactionRepository, never()).save(any(TransactionModel.class));
    }
}