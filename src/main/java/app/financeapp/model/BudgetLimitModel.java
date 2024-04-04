package app.financeapp.model;

import app.financeapp.model.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @JsonBackReference
    private AccountModel account;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "TYPE")
    private TransactionType type;

    @Column(name = "UPPER_LIMIT", nullable = false)
    private BigDecimal upperLimit;

    @Column(name = "USED_LIMIT", nullable = false)
    private BigDecimal usedLimit;// = new BigDecimal(0);

}
