package app.financeapp.controller;

import app.financeapp.model.TransactionModel;
import app.financeapp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final AccountController accountController;

    @GetMapping("/all")
    public ResponseEntity<List<TransactionModel>> getAll(){
        return ResponseEntity.ok(transactionRepository.findAll());
    }

}
