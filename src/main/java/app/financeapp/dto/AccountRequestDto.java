package app.financeapp.dto;

import app.financeapp.account.AccountType;
import app.financeapp.deposit.DepositModel;
import app.financeapp.user.UserModel;
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
