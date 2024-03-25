package app.financeapp.dto;

import app.financeapp.model.DepositModel;
import app.financeapp.model.UserModel;
import app.financeapp.model.enums.AccountType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@NoArgsConstructor
public class AccountRequestDto {
    private String number;
    private UserModel owner;
    private BigDecimal balance;
    private AccountType type;
    private List<DepositModel> deposits;
}
