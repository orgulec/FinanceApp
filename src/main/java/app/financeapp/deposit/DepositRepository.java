package app.financeapp.deposit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository  extends JpaRepository<DepositModel, Long> {
}
