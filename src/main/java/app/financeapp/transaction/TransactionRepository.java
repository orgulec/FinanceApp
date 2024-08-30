package app.financeapp.transaction;

import app.financeapp.account.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface TransactionRepository  extends JpaRepository<TransactionModel, Long> {

    @Query(value = "SELECT t FROM TransactionModel t WHERE t.toAccount = :account OR t.fromAccount = :account ")
    List<TransactionModel> findAllByAccount(AccountModel account);
}
