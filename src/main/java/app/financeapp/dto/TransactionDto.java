package app.financeapp.dto;

import app.financeapp.model.AccountModel;
import app.financeapp.model.enums.TransactionType;
import app.financeapp.model.enums.TransactionsStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private String title;
    private BigDecimal amount;
    private AccountModel fromAccount;
    private AccountModel toAccount;
    private ZonedDateTime transactionDate;
    private TransactionType transactionType;
    private TransactionsStatus status;
}
