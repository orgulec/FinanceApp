package app.financeapp.dto;

import app.financeapp.model.UserModel;
import app.financeapp.model.enums.AccountType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
public class AccountNewDto {
    private UserModel owner;
    private AccountType type;
}
