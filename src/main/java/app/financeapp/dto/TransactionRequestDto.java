package app.financeapp.dto;

import app.financeapp.account.AccountModel;
import app.financeapp.transaction.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
public class TransactionRequestDto {
    @NotNull
    private BigDecimal amount;
    @NotNull
    private AccountModel fromAccount;
    @NotNull
    private AccountModel toAccount;
    @NotNull
    private TransactionType transactionType;
    @NotNull
    private String title;

}
