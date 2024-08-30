package app.financeapp.deposit;

import org.springframework.data.jpa.repository.JpaRepository;

interface DepositRepository  extends JpaRepository<DepositModel, Long> {
}
