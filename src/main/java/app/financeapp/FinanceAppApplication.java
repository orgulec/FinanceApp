package app.financeapp;

import app.financeapp.repository.AccountRepository;
import app.financeapp.repository.BudgetLimitRepository;
import app.financeapp.repository.UserDataRepository;
import app.financeapp.repository.UserRepository;
import app.financeapp.utils.initservice.InitService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
    @Override
    public void run(String... args) {

        /*
         * Creates first entities into DB for tests
         */
        InitService initService = new InitService(userRepository,userDataRepository,accountRepository,budgetLimitRepository);
        initService.generateStartRecordsToDatabase();

    }
}
