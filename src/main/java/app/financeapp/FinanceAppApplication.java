package app.financeapp;

import app.financeapp.account.AccountRepository;
import app.financeapp.budget.BudgetLimitRepository;
import app.financeapp.user.UserDataRepository;
import app.financeapp.user.UserRepository;
import app.financeapp.utils.initservice.InitService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@SpringBootApplication
public class FinanceAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceAppApplication.class, args);
    }


    /*
     * Creates first entities into DB for tests
     */
    @Bean
    public CommandLineRunner commandLineRunner(
            UserRepository userRepository,
            UserDataRepository userDataRepository,
            AccountRepository accountRepository,
            BudgetLimitRepository budgetLimitRepository){
        return args -> {
            InitService initService = new InitService(userRepository,userDataRepository,accountRepository,budgetLimitRepository);
            initService.generateStartRecordsToDatabase();
        };
    }
}
