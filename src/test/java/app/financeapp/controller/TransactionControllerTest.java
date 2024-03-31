package app.financeapp.controller;

import app.financeapp.model.TransactionModel;
import app.financeapp.repository.TransactionRepository;
import app.financeapp.utils.exceptions.NoTransactionFoundException;
import net.bytebuddy.description.type.TypeList;
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
    private TransactionRepository transactionRepository;
    @InjectMocks
    TransactionController transactionController;
    @Test
    void getAll_shouldReturnListOfTransactionsWithStatus200() {
        //given
        TransactionModel transaction = new TransactionModel();
        List<TransactionModel> transactionList = new ArrayList<>();
            transactionList.add(transaction);

        //when
        when(transactionRepository.findAll()).thenReturn(transactionList);

        //then
        ResponseEntity<List<TransactionModel>> result = transactionController.getAll();
        assertAll(
                ()->assertEquals(HttpStatus.OK, result.getStatusCode()),
                ()->assertEquals(transactionList.size(), result.getBody().size()),
                ()->assertEquals(transactionList, result.getBody())
        );
    }
    @Test
    void getAll_shouldThrowNoTransactionFoundException() {
        //given
        List<TransactionModel> transactionList;
        String ex = "No transactions founded.";
        //when
        when(transactionRepository.findAll()).thenThrow(new NoTransactionFoundException(ex));

        //then
        assertThrows(NoTransactionFoundException.class, ()->transactionController.getAll());
    }
}