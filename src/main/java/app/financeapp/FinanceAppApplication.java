package app.financeapp;

import app.financeapp.model.AccountModel;
import app.financeapp.model.BudgetLimitModel;
import app.financeapp.model.UserData;
import app.financeapp.model.UserModel;
import app.financeapp.model.enums.AccountType;
import app.financeapp.model.enums.TransactionType;
import app.financeapp.repository.AccountRepository;
import app.financeapp.repository.BudgetLimitRepository;
import app.financeapp.repository.UserDataRepository;
import app.financeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@SpringBootApplication
public class FinanceAppApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(FinanceAppApplication.class, args);
    }

    private final UserRepository userRepository;
    private final UserDataRepository userDataRepository;
    private final AccountRepository accountRepository;
    private final BudgetLimitRepository budgetLimitRepository;

    /**
     * Creates first entities into DB for tests
     */
    @Override
    public void run(String... args) throws Exception {
        UserData data1 = new UserData(1L, "Nowak", "Kowalski", "Polish", LocalDate.of(1953, 12, 1), "338763592", "Legionistow", "46", "Lanckorona", "34-143", "Poland", ZonedDateTime.now());
        UserData data2 = new UserData(2L, "Sulik", "Twardoch", "Polish", LocalDate.of(1950, 10, 16), "510263485", "Kremerowska", "4/6", "Kraków", "31-130", "Poland", ZonedDateTime.now());
        userDataRepository.saveAll(List.of(data1, data2));

        UserModel user1 = new UserModel(1L, "Janek", "Kos", ZonedDateTime.now(), data1, new ArrayList<>());
        UserModel user2 = new UserModel(2L, "Maciej", "Zzadupia", ZonedDateTime.now(), data2, new ArrayList<>());
        userRepository.saveAll(List.of(user1, user2));

        AccountModel account1 = new AccountModel(1L, "55324687454900006581321456", user1, AccountType.CASH, "jankos", "password", BigDecimal.valueOf(15000.50), new ArrayList<>());
        AccountModel account2 = new AccountModel(2L, "51264687454912006581924168", user2, AccountType.CASH, "maciejos", "password2", BigDecimal.valueOf(15000.95), new ArrayList<>());
        accountRepository.saveAll(List.of(account1, account2));

        BudgetLimitModel budget1 = new BudgetLimitModel(1L, account1, "Rozrywka", TransactionType.ENTERTAINMENT, new BigDecimal(7500), new BigDecimal(2000));
        BudgetLimitModel budget2 = new BudgetLimitModel(2L, account1, "Zakupy", TransactionType.SHOPPING, new BigDecimal(3000), new BigDecimal(2500));
        BudgetLimitModel budget3 = new BudgetLimitModel(3L, account1, "Oszczędności", TransactionType.SAVINGS, new BigDecimal(8000), new BigDecimal(2500));
        budgetLimitRepository.saveAll(List.of(budget1, budget2, budget3));

    }
}
