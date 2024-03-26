package app.financeapp.repository;

import app.financeapp.dto.LoginDto;
import app.financeapp.model.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository  extends JpaRepository<AccountModel, Long> {
    List<AccountModel> findAllByOwner_Id(Long ownerId);
    List<AccountModel> findAllByLoginAndPassword(String login, String password);
}
