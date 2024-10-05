package app.financeapp.budget;

import app.financeapp.account.AccountModel;
import app.financeapp.account.AccountRepository;
import app.financeapp.account.AccountType;
import app.financeapp.dto.BudgetLimitDto;
import app.financeapp.dto.BudgetLimitResponseDto;
import app.financeapp.dto.TransactionRequestDto;
import app.financeapp.transaction.TransactionType;
import app.financeapp.user.UserModel;
import app.financeapp.utils.exceptions.AccountNotFoundException;
import app.financeapp.utils.exceptions.BudgetLimitAlreadyExistException;
import app.financeapp.utils.exceptions.BudgetNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BudgetLimitServiceTest {

    @Mock
    BudgetLimitRepository budgetLimitRepository;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    BudgetLimitService budgetLimitService;

    AccountModel account = new AccountModel(
            1L, "54321212345678909876543210", new UserModel(), AccountType.CASH, "AccountUser", "pass", new BigDecimal(1), new ArrayList<>());
    List<BudgetLimitModel> budgets = new ArrayList<>();

    @BeforeEach
    void setUp() {
        budgets = List.of(
                new BudgetLimitModel(1L, account, "Budget test 1", TransactionType.OTHERS, new BigDecimal(100), new BigDecimal(0)),
                new BudgetLimitModel(2L, account, "Budget test 2", TransactionType.SHOPPING, new BigDecimal(500), new BigDecimal(0))
        );
    }

    @Test
    void getAllByAccountId_shouldReturnListOfBudgetLimitModels() {
        //given
        Long accountId = 1L;
        //when
        when(budgetLimitRepository.findAllByAccount_Id(accountId)).thenReturn(budgets);
        //then
        List<BudgetLimitModel> result = budgetLimitService.getAllByAccountId(accountId);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(budgets.size(), result.size())
        );
    }

    @Test
    void getStatisticsById_shouldReturnListOfStrings() {
        //given
        Long accountId = 1L;
        //when
        when(budgetLimitRepository.findAllByAccount_Id(accountId)).thenReturn(budgets);
        //then
        List<String> expected = budgets.stream().map(a -> budgetLimitService.showStatistics(a)).toList();
        List<String> result = budgetLimitService.getStatisticsById(accountId);
        assertAll(
                () -> assertEquals(expected.size(), result.size()),
                () -> assertEquals(expected.get(0), result.get(0))
        );

    }

    @Test
    void getByBudgetId_shouldReturnBudgetLimitModel() {
        //given
        Long budgetId = 1L;
        //when
        when(budgetLimitRepository.findById(budgetId)).thenReturn(Optional.of(budgets.get(0)));
        //then
        BudgetLimitModel result = budgetLimitService.getByBudgetId(budgetId);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(budgets.get(0), result)
        );
    }

    @Test
    void getByBudgetId_shouldThrowExceptionWhenBudgetIdNotFound() {
        //given
        Long budgetId = 99L;
        //when
        when(budgetLimitRepository.findById(budgetId)).thenReturn(Optional.empty());
        //then
        assertThrows(BudgetNotFoundException.class, () -> budgetLimitService.getByBudgetId(budgetId));
    }

    @Test
    void showStatistics_shouldReturnString() {
        //given
        BudgetLimitResponseDto dto = new BudgetLimitResponseDto();
        dto.setTitle(budgets.get(0).getTitle());
        dto.setType(budgets.get(0).getType());
        dto.setUsedLimit(budgets.get(0).getUsedLimit());
        dto.setUpperLimit(budgets.get(0).getUpperLimit());
        //when
        //then
        String expected = dto.showStats();
        String result = budgetLimitService.showStatistics(budgets.get(0));
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expected, result)
        );
    }

    @Test
    void addNew_shouldCreateNewBudgetLimitModel() {
        //given
        BudgetLimitDto dto = new BudgetLimitDto();
        dto.setAccount(budgets.get(0).getAccount());
        dto.setTitle(budgets.get(0).getTitle());
        dto.setType(budgets.get(0).getType());
        dto.setLimit(budgets.get(0).getUpperLimit());
        //when
        when(accountRepository.findById(dto.getAccount().getId())).thenReturn(Optional.of(account));
        when(budgetLimitRepository.findAllByAccount_Id(1L)).thenReturn(new ArrayList<>());
        when(budgetLimitRepository.save(any(BudgetLimitModel.class))).thenReturn(budgets.get(0));
        //then
        BudgetLimitModel result = budgetLimitService.addNew(dto);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(BigDecimal.valueOf(100), result.getUpperLimit()),
                () -> assertEquals(BigDecimal.valueOf(0), result.getUsedLimit())
        );
    }

    @Test
    void addNew_shouldThrowExceptionWhenNoAccountFounded() {
        //given
        BudgetLimitDto dto = new BudgetLimitDto();
        dto.setAccount(budgets.get(0).getAccount());
        dto.setTitle(budgets.get(0).getTitle());
        dto.setType(budgets.get(0).getType());
        dto.setLimit(budgets.get(0).getUpperLimit());
        //when
        when(accountRepository.findById(dto.getAccount().getId())).thenReturn(Optional.empty());
        //then
        assertThrows(AccountNotFoundException.class, () -> budgetLimitService.addNew(dto));
    }

    @Test
    void addNew_shouldThrowExceptionsWhenBudgetAlreadyExists() {
        //given
        BudgetLimitDto dto = new BudgetLimitDto();
        dto.setAccount(budgets.get(0).getAccount());
        dto.setTitle(budgets.get(0).getTitle());
        dto.setType(budgets.get(0).getType());
        dto.setLimit(budgets.get(0).getUpperLimit());
        //when
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(budgetLimitRepository.findAllByAccount_Id(1L)).thenReturn(budgets);
        //then
        assertThrows(BudgetLimitAlreadyExistException.class, () -> budgetLimitService.addNew(dto));

    }

    @Test
    void getLimitByAccountAndType_shouldReturnFoundedBudgetLimit() {
        //given

        //when
        when(budgetLimitRepository.findAllByAccount_Id(account.getId())).thenReturn(budgets);
        //then
        BudgetLimitModel result = budgetLimitService.getLimitByAccountAndType(account.getId(), TransactionType.OTHERS);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(budgets.get(0), result)
        );
    }

    @Test
    void addTransactionToBudgetLimit_shouldSaveBudgetLimitInRepository() {
        //given
        BudgetLimitModel budget = budgets.get(0);
        TransactionRequestDto transaction = new TransactionRequestDto();
            transaction.setFromAccount(account);
            transaction.setToAccount(new AccountModel());
            transaction.setAmount(BigDecimal.valueOf(100));
            transaction.setTitle("Title 1");
            transaction.setTransactionType(TransactionType.ENTERTAINMENT);

        //when
        when(budgetLimitRepository.save(budget)).thenReturn(budget);
        //then
        BudgetLimitModel result = budgetLimitService.addTransactionToBudgetLimit(transaction, budget);
        verify(budgetLimitRepository).save(budget);
        assertAll(
                () -> assertNotNull(result),
                ()-> assertEquals(budget, result)
        );
    }

    @Test
    void saveBudget_shouldSaveBudgetLimitInRepository() {
        //given
        BudgetLimitModel budget = budgets.get(0);
        //when
        when(budgetLimitRepository.save(budget)).thenReturn(budget);
        //then
        budgetLimitService.saveBudget(budget);
        verify(budgetLimitRepository).save(budget);
    }
}