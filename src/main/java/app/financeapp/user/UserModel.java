package app.financeapp.user;

import app.financeapp.account.AccountModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.apache.tomcat.util.digester.ArrayStack;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_MODEL")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "FIRSTNAME", length = 128, nullable = false)
    private String firstName;

    @Column(name = "LASTNAME", length = 128, nullable = false)
    private String lastName;

    @Column(name = "CREATION_DATE")
    private ZonedDateTime creationDate;

    @OneToOne
    @JoinColumn(name = "data_id", referencedColumnName = "id")
    private UserData userData;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonBackReference
    private List<AccountModel> accounts = new ArrayStack<>();


    @PrePersist
    protected void onCreate() {
        creationDate = ZonedDateTime.now();
    }
}
