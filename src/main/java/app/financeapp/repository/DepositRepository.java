package app.financeapp.repository;

import app.financeapp.model.DepositModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository  extends JpaRepository<DepositModel, Long> {
}
