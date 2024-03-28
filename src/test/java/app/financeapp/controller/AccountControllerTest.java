package app.financeapp.controller;

import app.financeapp.dto.AccountRequestDto;
import app.financeapp.dto.TransactionDto;
import app.financeapp.dto.UserDto;
import app.financeapp.model.enums.TransactionType;
import app.financeapp.service.AccountService;
import app.financeapp.service.TransactionService;
import app.financeapp.utils.mappers.TransactionMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;
    @Mock
    private TransactionService transactionService;
//    @Mock
//    private TransactionMapper transactionMapper;
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
    void getAllTransactionsByAccountId() {
        //given
        Long id = 1L;
        TransactionDto transactionDto = new TransactionDto();
        ArrayList<TransactionDto> transactionList = new ArrayList<>();
            transactionList.add(transactionDto);
        //when
        when(transactionService.getAllByAccountId(id)).thenReturn(transactionList);

        //then
        ResponseEntity<List<TransactionDto>> result = accountController.getAllTransactionsByAccountId(id);
        assertAll(
                ()->assertEquals(HttpStatus.OK, result.getStatusCode()),
                ()->assertEquals(ResponseEntity.ok(transactionList), result),
                ()->assertEquals(transactionList,result.getBody()),
                ()->assertEquals(transactionList.size(),result.getBody().size())
        );
    }

    @Test
    void getAllTransactionsByAccountIdAndType() {
        //given
        Long id = 1L;
        String type = "INCOME";
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType(TransactionType.INCOME);
        List<TransactionDto> transactionList = new ArrayList<>();
        transactionList.add(transactionDto);
        //when
        when(transactionService.getAllByAccountIdAndType(id, type)).thenReturn(transactionList);

        //then
        ResponseEntity<List<TransactionDto>> result = accountController.getAllTransactionsByAccountIdAndType(id, type);
        assertAll(
                ()->assertEquals(HttpStatus.OK, result.getStatusCode()),
                ()->assertEquals(ResponseEntity.ok(transactionList), result),
                ()->assertEquals(transactionList,result.getBody()),
                ()->assertEquals(transactionList.size(),result.getBody().size())
        );
    }

    @Test
    void makeTransaction() {
        //given


        //when


        //then
    }

    @Test
    void createNewAccountForUser() {
        //given


        //when


        //then
    }

    @Test
    void createNewDepositForUser() {
        //given


        //when


        //then
    }
}