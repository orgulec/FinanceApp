package app.financeapp.model;

import app.financeapp.model.enums.TransactionType;
import app.financeapp.model.enums.TransactionsStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRANSACTIONS")
public class TransactionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "from_id")
    @JsonBackReference
    private AccountModel fromAccount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "to_id")
    @JsonBackReference
    private AccountModel toAccount;

    @Column(name = "TRANSACTION_DATE", nullable = false)
    private ZonedDateTime transactionDate;

    @Column(name = "TRANSACTION_TYPE")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private TransactionsStatus status;

    @Column(name = "TITLE")
    private String title;

    @PrePersist
    protected void onCreate() {
        transactionDate = ZonedDateTime.now();
    }

}
