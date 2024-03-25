package app.financeapp.service;

import app.financeapp.dto.AccountNewDto;
import app.financeapp.dto.AccountRequestDto;
import app.financeapp.dto.LoginDto;
import app.financeapp.model.AccountModel;
import app.financeapp.model.UserModel;
import app.financeapp.repository.AccountRepository;
import app.financeapp.utils.mappers.AccountMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final AccountMapper mapper;

    public AccountRequestDto getByIdDto(Long id) {
        Optional<AccountModel> accountOpt = accountRepository.findById(id);
        if(accountOpt.isEmpty()){
            throw new EntityNotFoundException("No Account founded.");
        }
        return mapper.toReqDto(accountOpt.get());
    }
    public AccountModel getById(Long id) {
        Optional<AccountModel> accountOpt = accountRepository.findById(id);
        if(accountOpt.isEmpty()){
            throw new EntityNotFoundException("No Account founded.");
        }
        return accountOpt.get();
    }

    public List<AccountRequestDto> getAllByUser(LoginDto loginDto) {
        List<AccountModel> accountsList = accountRepository.findAllByLoginAndPassword(loginDto.getLogin(), loginDto.getPassword());
        if(accountsList.isEmpty()){
            throw new EntityNotFoundException("No accounts founded.");
        }
        List<AccountRequestDto> accountDto = new ArrayList<>();

        accountsList.forEach(a -> {
            accountDto.add(mapper.toReqDto(a));
        });
        return accountDto;
    }

    public AccountModel substractBalance(Long id, BigDecimal amount){
        AccountModel account = getById(id);
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }
    public AccountModel addBalance(Long id, BigDecimal amount){
        AccountModel account = getById(id);
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }

    public AccountModel addNewAccountToUser(AccountNewDto newDto) {
        UserModel user = userService.getUserById(newDto.getOwner().getId());
        AccountModel newAccount = new AccountModel();

        newAccount.setOwner(user);
        newAccount.setType(newDto.getType());
        newAccount.setAccountNumber(generateAccountNumber());
        newAccount.setBalance(BigDecimal.ZERO);
        return newAccount;
    }

    private String generateAccountNumber() {
        //TODO
        return "12363214523678954400021458";
    }
}
