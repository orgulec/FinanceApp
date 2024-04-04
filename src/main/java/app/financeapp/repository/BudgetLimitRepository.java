package app.financeapp.repository;

import app.financeapp.model.BudgetLimitModel;
import app.financeapp.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetLimitRepository extends JpaRepository<BudgetLimitModel, Long> {
    List<BudgetLimitModel> findAllByAccount_Id(Long id);
}
