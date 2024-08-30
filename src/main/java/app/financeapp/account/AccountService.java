package app.financeapp.account;

import app.financeapp.dto.AccountNewDto;
import app.financeapp.dto.AccountRequestDto;
import app.financeapp.dto.UserDto;
import app.financeapp.user.UserModel;
import app.financeapp.user.UserService;
import app.financeapp.utils.enums.ExceptionMsg;
import app.financeapp.utils.exceptions.IncorrectBalanceValueException;
import app.financeapp.utils.mappers.AccountMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Data
@Primary
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;
    private final AccountMapper accountMapper;

    public AccountRequestDto getByIdDto(@NotNull Long id) {
        AccountModel account = getById(id);
        return accountMapper.toReqDto(account);
    }

    public AccountModel getById(@NotNull Long id) {
        Optional<AccountModel> accountOpt = accountRepository.findById(id);
        if(accountOpt.isEmpty()){
            throw new EntityNotFoundException(ExceptionMsg.NO_ACCOUNT_FOUNDED.toString());
        }
        return accountOpt.get();
    }

    public List<AccountRequestDto> getAllByUser(@NotNull UserDto userDto) {
        List<AccountModel> accountsList = accountRepository.findAllByOwner_Id(userDto.getId());
        if(accountsList.isEmpty()){
            throw new EntityNotFoundException(ExceptionMsg.NO_ACCOUNT_FOUNDED.toString());
        }
        return accountsList.stream().map(
                accountMapper::toReqDto)
                .collect(Collectors.toList());
    }

    public void subtractBalance(@NotNull Long id, BigDecimal amount){
        AccountModel account = getById(id);
        if(account.getBalance().compareTo(amount)<0){
            throw new IncorrectBalanceValueException(ExceptionMsg.INSUFFICIENT_FUNDS_IN_ACCOUNT.toString());
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    public void addBalance(@NotNull Long id, BigDecimal amount){
        AccountModel account = getById(id);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    public AccountModel addNewAccountToUser(@NotNull AccountNewDto newDto) {
        UserModel user = userService.getUserById(newDto.getOwner().getId());
        AccountModel newAccount = new AccountModel();
        String newAccountNumber = generateRandomAccountNumber();
        while(checkIfAccountNumberIsAvailable(newAccountNumber)){
            newAccountNumber = generateRandomAccountNumber();
        }
        newAccount.setOwner(user);
        newAccount.setAccountNumber(newAccountNumber);
        newAccount.setBalance(BigDecimal.valueOf(0));
        newAccount.setType(newDto.getType());
        newAccount.setLogin(newDto.getLogin());
        newAccount.setPassword(newDto.getPassword());
        return accountRepository.save(newAccount);
    }

    private String generateRandomAccountNumber() {
        StringBuilder number = new StringBuilder();
        for(int i = 0; i<26; i++){
            int x = new Random().nextInt(9);
            number.append(x);
        }
        return number.toString();
    }

    private boolean checkIfAccountNumberIsAvailable(String number){
        Optional<AccountModel> byAccountNumber = accountRepository.findByAccountNumber(number);
        return byAccountNumber.isPresent();
    }

}
