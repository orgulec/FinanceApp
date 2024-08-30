package app.financeapp.service;

import app.financeapp.account.*;
import app.financeapp.dto.AccountNewDto;
import app.financeapp.dto.AccountRequestDto;
import app.financeapp.dto.DepositDto;
import app.financeapp.dto.UserDto;
import app.financeapp.deposit.DepositModel;
import app.financeapp.transaction.TransactionModel;
import app.financeapp.user.UserModel;
import app.financeapp.deposit.DepositRepository;
import app.financeapp.transaction.TransactionRepository;
import app.financeapp.user.UserService;
import app.financeapp.utils.exceptions.IncorrectBalanceValueException;
import app.financeapp.utils.mappers.AccountMapper;
import app.financeapp.utils.mappers.DepositMapper;
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
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private DepositRepository depositRepository;
    @Mock
    private UserService userService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private DepositMapper depositMapper;
    @InjectMocks
    private AccountService accountService;

    @Test
    void getByIdDto_shouldReturnAccountRequestDto() {
        //given
        Long id = 1L;
        String number = "1234567890";
        AccountModel account = new AccountModel();
            account.setAccountNumber(number);
        AccountRequestDto accountDto = new AccountMapper().toReqDto(account);

        //when
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(accountMapper.toReqDto(account)).thenReturn(accountDto);

        //then
        AccountRequestDto result = accountService.getByIdDto(id);
        assertAll(
                ()->assertNotNull(result),
                ()->assertEquals(accountDto, result),
                ()->assertEquals(number, result.getNumber())
        );
    }

    @Test
    void getByIdDto_shouldThrowEntityNotFoundExceptionIfOptionalIsEmpty() {
        //given
        Long id = 1L;

        //when
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, ()-> accountService.getByIdDto(id));
    }

    @Test
    void getAllByUser_shouldReturnListOfAccountRequestDtos() {
        //given
        Long userId = 1L;
        String accountNumber = "1234567890";
        UserDto userDto = new UserDto();
            userDto.setId(userId);
        AccountModel account = new AccountModel();
            account.setAccountNumber(accountNumber);
        List<AccountModel> accountsList = new ArrayList<>();
            accountsList.add(account);
        AccountRequestDto accountDto = new AccountMapper().toReqDto(account);
        List<AccountRequestDto> accountsDtoList = new ArrayList<>();
            accountsDtoList.add(accountDto);

        //when
        when(accountRepository.findAllByOwner_Id(userDto.getId())).thenReturn(accountsList);
        when(accountMapper.toReqDto(account)).thenReturn(accountDto);
        //then
        List<AccountRequestDto> result = accountService.getAllByUser(userDto);
        assertAll(
                () -> assertEquals(accountsDtoList, result),
                () -> assertEquals(accountsDtoList.size(), result.size()),
                () -> assertEquals(accountsDtoList.get(0), result.get(0))
        );
    }

    @Test
    void getAllByUser_shouldThrowEntityNotFoundExceptionIfListIsEmpty() {
        //given
        Long userId = 1L;
        UserDto userDto = new UserDto();
            userDto.setId(userId);

        //when
        when(accountRepository.findAllByOwner_Id(userDto.getId())).thenThrow(new EntityNotFoundException("No accounts founded."));
        //then
        assertThrows(EntityNotFoundException.class, ()->accountService.getAllByUser(userDto));
    }

    @Test
    void subtractBalance_shouldSaveChangedAccountBalance() {
        //given
        Long id = 1L;
        BigDecimal amount = new BigDecimal(25);
        BigDecimal balanceResult = new BigDecimal(75);
        AccountModel accountBalance = new AccountModel();
            accountBalance.setBalance(new BigDecimal(100));
        AccountModel accountNewBalance = new AccountModel();
            accountNewBalance.setBalance(balanceResult);

        //when
        when(accountRepository.findById(id)).thenReturn(Optional.of(accountBalance));
        when(accountRepository.save(any(AccountModel.class))).thenReturn(accountNewBalance);

        //then
        accountService.subtractBalance(id, amount);
        verify(accountRepository).save(accountBalance);
    }

    @Test
    void subtractBalance_shouldThrowIncorrectBalanceValueException() {
        //given
        Long id = 1L;
        BigDecimal amount = new BigDecimal(100);
        AccountModel accountBalance = new AccountModel();
            accountBalance.setBalance(new BigDecimal(10));

        //when
        when(accountRepository.findById(id)).thenReturn(Optional.of(accountBalance));

        //then
        assertThrows(IncorrectBalanceValueException.class, ()->accountService.subtractBalance(id, amount));
        verify(accountRepository, never()).save(any(AccountModel.class));
    }

    @Test
    void addBalance_shouldSaveChangedAccountBalance() {
        //given
        Long id = 1L;
        BigDecimal amount = new BigDecimal(100);
        AccountModel accountBalance = new AccountModel();
        accountBalance.setBalance(new BigDecimal(25));

        //when
        when(accountRepository.findById(any(Long.class))).thenReturn(Optional.of(accountBalance));
        when(accountRepository.save(accountBalance)).thenReturn(accountBalance);

        //then
        accountService.addBalance(id, amount);
        verify(accountRepository).save(accountBalance);
    }

    @Test
    void addNewAccountToUser_shouldReturnNewAccountModelForUserId() {
        //given
        UserModel user = new UserModel();
            user.setFirstName("Jan");
            user.setLastName("Nowak");
        AccountNewDto accountDto = new AccountNewDto();
            accountDto.setOwner(user);
        AccountModel newAccount = new AccountModel();
            newAccount.setOwner(user);
        //when
        when(userService.getUserById(user.getId())).thenReturn(user);
        when(accountRepository.save(any(AccountModel.class))).thenReturn(newAccount);
        //then
        AccountModel result = accountService.addNewAccountToUser(accountDto);

        verify(accountRepository).save(any(AccountModel.class));
        assertAll(
                ()->assertNotNull(result),
                ()->assertEquals(newAccount.getId(), result.getId()),
                ()->assertEquals(newAccount.getOwner(), result.getOwner())
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
        when(accountRepository.findById(depositDto.getAccountId())).thenReturn(Optional.of(account));
        when(depositMapper.toModel(depositDto)).thenReturn(newDeposit);
        when(depositRepository.save(newDeposit)).thenReturn(newDeposit);
        //then
        DepositModel result = accountService.addNewDepositToUser(depositDto);
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
        when(accountRepository.findById(fromAccount.getId())).thenReturn(Optional.of(fromAccount));
        when(transactionRepository.save(any(TransactionModel.class))).thenReturn(transaction);
        //then
        accountService.transferToDeposit(newDeposit, fromAccount);
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
            () -> accountService.transferToDeposit(newDeposit, fromAccount));
        verify(transactionRepository, never()).save(any(TransactionModel.class));
    }
}