package app.financeapp.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository  extends JpaRepository<AccountModel, Long> {
    List<AccountModel> findAllByOwner_Id(Long ownerId);
    Optional<AccountModel> findByAccountNumber(String number);
}
