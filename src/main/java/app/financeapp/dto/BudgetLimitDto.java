package app.financeapp.dto;

import app.financeapp.model.AccountModel;
import app.financeapp.model.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
public class BudgetLimitDto {
    @NotNull
    private AccountModel account;
    @NotNull
    private TransactionType type;
    @NotNull
    private BigDecimal limit;

    private String title;

}
