package app.financeapp.model;

import app.financeapp.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BUDGET_LIMIT")
public class BudgetLimitModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne
    private AccountModel account;

//    @OneToMany
//    private TransactionModel transaction;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "TYPE", nullable = false)
    private TransactionType type;

    @Column(name = "LIMIT", nullable = false)
    private BigDecimal limit;

    @Column(name = "USED_LIMIT")
    private BigDecimal usedLimit = new BigDecimal(0);

}
