package app.financeapp.service;

import app.financeapp.dto.*;
import app.financeapp.model.AccountModel;
import app.financeapp.model.DepositModel;
import app.financeapp.model.TransactionModel;
import app.financeapp.model.UserModel;
import app.financeapp.model.enums.ExceptionMsg;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
        List<AccountRequestDto> accountDto = new ArrayList<>();
        accountsList.forEach(
                a -> accountDto.add(accountMapper.toReqDto(a))
        );
        return accountDto;
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

    @Transactional
    public DepositModel addNewDepositToUser(@Valid DepositDto deposit) {
        AccountModel account = accountRepository.findById(deposit.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMsg.NO_ACCOUNT_FOUNDED.toString()));
        DepositModel newDeposit = depositMapper.toModel(deposit);
        newDeposit.setAccount(account);

        transferToDeposit(newDeposit, account);
        return depositRepository.save(newDeposit);
    }

    public void transferToDeposit(DepositModel newDeposit, AccountModel fromAccount) {
        if(fromAccount.getBalance().compareTo(newDeposit.getBalance())<0){
            throw new IncorrectBalanceValueException(ExceptionMsg.INSUFFICIENT_FUNDS_IN_ACCOUNT.toString());
        }
        String title = "Own deposit transfer.";
        TransactionModel transaction = new TransactionModel();

        transaction.setTransactionType(TransactionType.SAVINGS);
        transaction.setTitle(title);
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(fromAccount);
        transaction.setAmount(newDeposit.getBalance());
        transaction.setStatus(TransactionsStatus.ACCEPTED);

        subtractBalance(fromAccount.getId(), newDeposit.getBalance());
        transactionRepository.save(transaction);
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
