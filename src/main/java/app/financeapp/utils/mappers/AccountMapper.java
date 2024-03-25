package app.financeapp.utils.mappers;

import app.financeapp.dto.AccountRequestDto;
import app.financeapp.model.AccountModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AccountMapper {

    public AccountRequestDto toReqDto(AccountModel model) {
        AccountRequestDto dto = new AccountRequestDto();
        dto.setNumber(model.getAccountNumber());
        dto.setOwner(model.getOwner());
        dto.setBalance(model.getBalance());
        dto.setType(model.getType());
        if(model.getDeposits()!=null) dto.setDeposits(model.getDeposits());
        else dto.setDeposits(new ArrayList<>());
        return dto;
    }
}