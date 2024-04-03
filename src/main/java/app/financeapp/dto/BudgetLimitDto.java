package app.financeapp.dto;

import app.financeapp.model.AccountModel;
import app.financeapp.model.enums.TransactionType;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
public class BudgetLimitDto {

    private AccountModel account;

//    @OneToMany
//    private TransactionModel transaction;

    private TransactionType type;

    private BigDecimal limit;

    private String title;

}
