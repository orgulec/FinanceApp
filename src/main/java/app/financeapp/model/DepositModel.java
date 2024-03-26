package app.financeapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DEPOSIT")
public class DepositModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @JsonBackReference
    private AccountModel account;

    @Column(name = "BALANCE", nullable = false)
    private BigDecimal balance;

    @Column(name = "CREATION_DATE", nullable = false)
    private ZonedDateTime creationDate;

    @Column(name = "PLANNED_END_DATE", nullable = false)
    private ZonedDateTime plannedEndDate;


    @PrePersist
    protected void onCreate() {
        creationDate = ZonedDateTime.now();
    }
}
