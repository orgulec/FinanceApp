package app.financeapp.account;

import app.financeapp.deposit.DepositModel;
import app.financeapp.user.UserModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ACCOUNT")
public class AccountModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "NUMBER", length = 26, nullable = false)
    private String accountNumber;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private UserModel owner;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Column(name = "LOGIN", length = 128, nullable = false)
    private String login;
    @Column(name = "PASSWORD", length = 128, nullable = false)
    private String password;

    @Column(name = "BALANCE", nullable = false, precision = 10)
    private BigDecimal balance;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<DepositModel> deposits = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountModel that = (AccountModel) o;
        return Objects.equals(id, that.id) && Objects.equals(accountNumber, that.accountNumber) && Objects.equals(owner, that.owner) && type == that.type && Objects.equals(login, that.login) && Objects.equals(password, that.password) && Objects.equals(balance, that.balance) && Objects.equals(deposits, that.deposits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, owner, type, login, password, balance, deposits);
    }
}
