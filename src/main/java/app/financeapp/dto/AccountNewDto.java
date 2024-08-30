package app.financeapp.dto;

import app.financeapp.account.AccountType;
import app.financeapp.user.UserModel;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
public class AccountNewDto {
    @NotNull
    private UserModel owner;
    @NotNull
    private AccountType type;
    private BigDecimal amount;
    private String login;
    private String password;
}
