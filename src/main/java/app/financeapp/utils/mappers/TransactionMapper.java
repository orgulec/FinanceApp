package app.financeapp.utils.mappers;

import app.financeapp.dto.TransactionDto;
import app.financeapp.model.TransactionModel;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class TransactionMapper {

    public TransactionDto toDto(TransactionModel model){
        TransactionDto dto = new TransactionDto();

        dto.setId(model.getId());
        dto.setAmount(model.getAmount());
        dto.setTitle(model.getTitle());
        if(model.getFromAccount()!=null) dto.setFromAccount(model.getFromAccount());
        if(model.getToAccount()!=null) dto.setToAccount(model.getToAccount());
        dto.setTransactionDate(model.getTransactionDate());
        dto.setTransactionType(model.getTransactionType());
        dto.setStatus(model.getStatus());
        return dto;
    }

}
