package app.financeapp.dto;

import app.financeapp.account.AccountModel;
import app.financeapp.transaction.TransactionType;
import app.financeapp.transaction.TransactionsStatus;
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
