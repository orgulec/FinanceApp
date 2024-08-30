package app.financeapp.deposit;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;

    public DepositModel saveDeposit(DepositModel newDeposit){
        return depositRepository.save(newDeposit);
    }

}
