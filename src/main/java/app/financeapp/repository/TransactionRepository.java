package app.financeapp.repository;

import app.financeapp.model.AccountModel;
import app.financeapp.model.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository  extends JpaRepository<TransactionModel, Long> {

    @Query(value = "SELECT t FROM TransactionModel t WHERE t.toAccount = :account OR t.fromAccount = :account ")
    List<TransactionModel> findAllByAccount(AccountModel account);
}
