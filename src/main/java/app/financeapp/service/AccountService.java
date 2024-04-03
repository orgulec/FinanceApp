package app.financeapp.service;

import app.financeapp.dto.*;
import app.financeapp.model.AccountModel;
import app.financeapp.model.DepositModel;
import app.financeapp.model.TransactionModel;
import app.financeapp.model.UserModel;
import app.financeapp.model.enums.TransactionType;
import app.financeapp.model.enums.TransactionsStatus;
import app.financeapp.repository.AccountRepository;
import app.financeapp.repository.DepositRepository;
import app.financeapp.repository.TransactionRepository;
import app.financeapp.utils.exceptions.IncorrectBalanceValueException;
import app.financeapp.utils.mappers.AccountMapper;
import app.financeapp.utils.mappers.DepositMapper;
import app.financeapp.utils.mappers.TransactionMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
@Primary
public class AccountService {
    private final AccountRepository accountRepository;
    private final DepositRepository depositRepository;
    private final TransactionRepository transactionRepository;

    private final UserService userService;

    private final AccountMapper accountMapper;
    private final DepositMapper depositMapper;
    private final TransactionMapper transactionMapper;

    public AccountRequestDto getByIdDto(Long id) {
        AccountModel account = getById(id);
        return accountMapper.toReqDto(account);
    }

    public AccountModel getById(Long id) {
        Optional<AccountModel> accountOpt = accountRepository.findById(id);
        if(accountOpt.isEmpty()){
            throw new EntityNotFoundException("No Account founded.");
        }
        return accountOpt.get();
    }

    public List<AccountRequestDto> getAllByUser(UserDto userDto) {
        List<AccountModel> accountsList = accountRepository.findAllByOwner_Id(userDto.getId());
        if(accountsList.isEmpty()){
            throw new EntityNotFoundException("No accounts founded.");
        }
        List<AccountRequestDto> accountDto = new ArrayList<>();

        accountsList.forEach(
                a -> accountDto.add(accountMapper.toReqDto(a))
        );
        return accountDto;
    }

    public void subtractBalance(Long id, BigDecimal amount){
        AccountModel account = getById(id);
        if(account.getBalance().compareTo(amount)<0){
            throw new IncorrectBalanceValueException("Insufficient funds in the account.");
        }
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    public void addBalance(Long id, BigDecimal amount){
        AccountModel account = getById(id);
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    public AccountModel addNewAccountToUser(AccountNewDto newDto) {
        UserModel user = userService.getUserById(newDto.getOwner().getId());
        AccountModel newAccount = new AccountModel();

        newAccount.setOwner(user);
        newAccount.setAccountNumber(generateAccountNumber());
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setType(newDto.getType());
        newAccount.setLogin(newDto.getLogin());
        newAccount.setPassword(newDto.getPassword());
        return accountRepository.save(newAccount);
    }

    @Transactional
    public DepositModel addNewDepositToUser(DepositDto deposit) {
        AccountModel account = accountRepository.findById(deposit.getAccountId()).orElseThrow(() -> new EntityNotFoundException("No account founded."));
        DepositModel newDeposit = depositMapper.toModel(deposit);
        newDeposit.setAccount(account);

        transferToDeposit(newDeposit, account);
        return depositRepository.save(newDeposit);
    }

    public void transferToDeposit(DepositModel newDeposit, AccountModel fromAccount) {
        if(fromAccount.getBalance().compareTo(newDeposit.getBalance())<0){
            throw new IncorrectBalanceValueException("Insufficient funds in the account.");
        }
        String title = "Own deposit transfer.";
        TransactionModel transaction = new TransactionModel();

        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTitle(title);
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(fromAccount);
        transaction.setAmount(newDeposit.getBalance());
        transaction.setStatus(TransactionsStatus.ACCEPTED);

        subtractBalance(fromAccount.getId(), newDeposit.getBalance());
        transactionRepository.save(transaction);
    }

    private String generateAccountNumber() {
        //TODO
        StringBuilder number = new StringBuilder();
        for(int i = 0; i<26; i++){
            double random = Math.random();
            Long x = Math.min(9,Math.round(random*10));
            number.append(x);
        }
        return number.toString();//"12363214523678954400021458";
    }

}
