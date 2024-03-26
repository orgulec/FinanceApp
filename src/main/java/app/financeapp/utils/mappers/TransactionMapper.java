package app.financeapp.utils.mappers;

import app.financeapp.dto.TransactionDto;
import app.financeapp.model.TransactionModel;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDto toDto(TransactionModel model){
        TransactionDto dto = new TransactionDto();

        dto.setId(model.getId());
        dto.setAmount(model.getAmount());
        dto.setTitle(model.getTitle());
        dto.setFromAccount(model.getFromAccount());
        dto.setToAccount(model.getToAccount());
        dto.setTransactionDate(model.getTransactionDate());
        dto.setTransactionType(model.getTransactionType());
        dto.setStatus(model.getStatus());
        return dto;
    }

}
