package app.financeapp.transaction;

import app.financeapp.dto.TransactionDto;
import app.financeapp.dto.TransactionRequestDto;
import app.financeapp.utils.exceptions.NoTransactionFoundException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    TransactionController transactionController;

    @Test
    void getAll_shouldReturnListOfTransactionsWithStatus200() {
        //given
        var transaction = new TransactionDto();
        List<TransactionDto> transactionList = List.of(transaction);

        //when
        when(transactionService.getAll()).thenReturn(transactionList);

        //then
        ResponseEntity<List<TransactionDto>> result = transactionController.getAll();
        assertAll(
                ()->assertEquals(HttpStatus.OK, result.getStatusCode()),
                ()->assertEquals(transactionList.size(), result.getBody().size()),
                ()->assertEquals(transactionList, result.getBody())
        );
    }

    @Test
    void getAll_shouldThrowNoTransactionFoundException() {
        //given
        String ex = "No transactions founded.";
        //when
        when(transactionService.getAll()).thenThrow(new NoTransactionFoundException(ex));

        //then
        assertThrows(NoTransactionFoundException.class, ()->transactionController.getAll());
    }


    @Test
    void getAllTransactionsByAccountId_shouldReturnListOfTransactionDtoWithStatus200() {
        //given
        Long id = 1L;
        TransactionDto transactionDto = new TransactionDto();
        ArrayList<TransactionDto> transactionList = new ArrayList<>();
        transactionList.add(transactionDto);
        //when
        when(transactionService.getAllByAccountId(id)).thenReturn(transactionList);

        //then
        ResponseEntity<List<TransactionDto>> result = transactionController.getAllTransactionsByAccountId(id);
        assertAll(
                ()->assertEquals(HttpStatus.OK, result.getStatusCode()),
                ()->assertEquals(ResponseEntity.ok(transactionList), result),
                ()->assertEquals(transactionList,result.getBody()),
                ()->assertEquals(transactionList.size(),result.getBody().size())
        );
    }

    @Test
    void getAllTransactionsByAccountIdAndType_shouldReturnListOfTransactionDtoWithStatus200() {
        //given
        Long id = 1L;
        String type = "INCOME";
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType(TransactionType.OTHERS);
        List<TransactionDto> transactionList = new ArrayList<>();
        transactionList.add(transactionDto);
        //when
        when(transactionService.getAllByAccountIdAndType(id, type)).thenReturn(transactionList);

        //then
        ResponseEntity<List<TransactionDto>> result = transactionController.getAllTransactionsByAccountIdAndType(id, type);
        assertAll(
                ()->assertEquals(HttpStatus.OK, result.getStatusCode()),
                ()->assertEquals(ResponseEntity.ok(transactionList), result),
                ()->assertEquals(transactionList,result.getBody()),
                ()->assertEquals(transactionList.size(),result.getBody().size())
        );
    }

    @Test
    void makeTransaction_shouldReturnTransactionModelWithStatus202() {
        //given
        TransactionRequestDto transactionDto = new TransactionRequestDto();
        TransactionModel transaction = new TransactionModel();

        //when
        when(transactionService.makeTransfer(transactionDto)).thenReturn(transaction);

        //then
        ResponseEntity<TransactionModel> result = transactionController.makeTransaction(transactionDto);
        assertAll(
                ()->assertEquals(HttpStatus.ACCEPTED, result.getStatusCode()),
                ()->assertEquals(transaction, result.getBody())
        );
    }

}