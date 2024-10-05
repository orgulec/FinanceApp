package app.financeapp.dto;

import app.financeapp.account.AccountModel;
import app.financeapp.transaction.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static app.financeapp.transaction.TransactionType.ENTERTAINMENT;

@Data
@NoArgsConstructor
public class BudgetLimitDto {
    @NotNull(message = "Account can not be empty")
    private AccountModel account;
    @NotNull(message = "Type can not be empty")
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @NotNull(message = "Limit can not be empty")
    @Min(1)
    private BigDecimal limit;
    @NotBlank(message = "Title can not be empty")
    private String title;

}
