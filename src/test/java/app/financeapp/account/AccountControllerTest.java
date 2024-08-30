package app.financeapp.account;

import app.financeapp.deposit.DepositModel;
import app.financeapp.dto.AccountNewDto;
import app.financeapp.dto.AccountRequestDto;
import app.financeapp.dto.DepositDto;
import app.financeapp.dto.UserDto;
import app.financeapp.transaction.TransactionService;
import app.financeapp.user.UserModel;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private TransactionService transactionService;
    @Mock
    private AccountService accountService;
    @InjectMocks
    private AccountController accountController;

    @Test
    void getById_shouldReturnAccountDtoWithStatus200() {
        //given
        Long id = 1L;
        AccountRequestDto account = new AccountRequestDto();
        //when
        when(accountService.getByIdDto(id)).thenReturn(account);
        //then
        ResponseEntity<AccountRequestDto> result = accountController.getById(id);
        assertAll(
                () -> assertEquals(ResponseEntity.ok(account), result),
                () -> assertEquals(HttpStatus.OK, result.getStatusCode())
        );
    }

    @Test
    void getById_shouldThrowEntityNotFoundExceptionWhenWrongId() {
        //given
        Long id = 1L;
        //when
        when(accountService.getByIdDto(id)).thenThrow(new EntityNotFoundException());
        //then
        assertThrows(EntityNotFoundException.class, () -> accountController.getById(id));
    }

    @Test
    void getAllByUser_shouldReturnListOfAccountDtosWithStatus200() {
        //given
        UserDto userDto = new UserDto();
            userDto.setId(1L);
            userDto.setFirstName("Jan");
            userDto.setLastName("Nowak");
        AccountRequestDto accountDto = new AccountRequestDto();
        List<AccountRequestDto> accountsList = new ArrayList<>();
            accountsList.add(accountDto);
        //when
        when(accountService.getAllByUser(userDto)).thenReturn(accountsList);

        //then
        ResponseEntity<List<AccountRequestDto>> result = accountController.getAllByUser(userDto);
        assertAll(
                ()->assertEquals(HttpStatus.OK, result.getStatusCode()),
                ()->assertEquals(ResponseEntity.ok(accountsList), result),
                ()->assertEquals(accountsList,result.getBody()),
                ()->assertEquals(accountsList.size(),result.getBody().size())
        );
    }


    @Test
    void createNewAccountForUser_shouldReturnNewAccountModelWithStatus201() {
        //given
        UserModel user = new UserModel();
        AccountNewDto accountDto = new AccountNewDto();
            accountDto.setOwner(user);
            accountDto.setType(AccountType.CASH);
        AccountModel account = new AccountModel();
            account.setOwner(user);
            account.setType(AccountType.CASH);

        //when
        when(accountService.addNewAccountToUser(accountDto)).thenReturn(account);

        //then
        ResponseEntity<AccountModel> result = accountController.createNewAccountForUser(accountDto);
        assertAll(
                ()->assertEquals(HttpStatus.CREATED, result.getStatusCode()),
                ()->assertEquals(account, result.getBody())
        );
    }

    @Test
    void createNewDepositForUser_shouldReturnNewDepositModelWithStatus201() {
        //given
        ZonedDateTime date = ZonedDateTime.now();
        BigDecimal balance = new BigDecimal(0);

        DepositDto depositDto = new DepositDto();
            depositDto.setPlannedEndDate(date);
            depositDto.setBalance(balance);
        DepositModel deposit = new DepositModel();
            deposit.setPlannedEndDate(date);
            deposit.setBalance(balance);

        //when
        when(transactionService.addNewDepositToUser(depositDto)).thenReturn(deposit);

        //then
        ResponseEntity<DepositModel> result = accountController.createNewDepositForUser(depositDto);
        assertAll(
                ()->assertEquals(HttpStatus.CREATED, result.getStatusCode()),
                ()->assertEquals(deposit, result.getBody())
        );
    }
}