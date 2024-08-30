package app.financeapp.utils.mappers;

import app.financeapp.dto.DepositDto;
import app.financeapp.deposit.DepositModel;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class DepositMapper {

    public DepositModel toModel(DepositDto dto){
        DepositModel model = new DepositModel();
        model.setBalance(dto.getBalance());
        model.setPlannedEndDate(dto.getPlannedEndDate());
        return model;
    }

}
