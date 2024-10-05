package app.financeapp.budget;

import app.financeapp.account.AccountModel;
import app.financeapp.account.AccountType;
import app.financeapp.dto.BudgetLimitDto;
import app.financeapp.dto.BudgetLimitResponseDto;
import app.financeapp.security.SecurityConfig;
import app.financeapp.transaction.TransactionType;
import app.financeapp.user.UserModel;
import app.financeapp.utils.exceptions.BudgetNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@WebMvcTest(BudgetLimitController.class)
@Import(SecurityConfig.class)
//@AutoConfigureMockMvc//(addFilters = false)
class BudgetLimitControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BudgetLimitService budgetService;

    AccountModel account = new AccountModel(
            1L, "54321212345678909876543210", new UserModel(), AccountType.CASH, "AccountUser", "pass", new BigDecimal(1), new ArrayList<>());
    List<BudgetLimitModel> budgets = new ArrayList<>();

    @BeforeEach
    void setUp() {
        budgets = List.of(
                new BudgetLimitModel(1L, null, "Budget test 1", TransactionType.OTHERS, new BigDecimal(100), new BigDecimal(0)),
                new BudgetLimitModel(2L, null, "Budget test 2", TransactionType.SHOPPING, new BigDecimal(500), new BigDecimal(0))
        );
    }

    @Test
    @WithMockUser("AccountUser")
    void getBudgetById_shouldFindOneBudgetByGivenId() throws Exception {

        String jsonResponse = """
                       {
                            "id": 1,
                            "title": "Budget test 1",
                            "type": "OTHERS",
                            "upperLimit": 100,
                            "usedLimit": 0
                       }
                """;

        when(budgetService.getByBudgetId(1L)).thenReturn(budgets.get(0));

        mockMvc.perform(get("/budget/byId/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    @WithMockUser("AccountUser")
    void getBudgetById_shouldThrowExceptionWhenGivenInvalidAccountId() throws Exception {

        when(budgetService.getByBudgetId(99L)).thenThrow(BudgetNotFoundException.class);

        mockMvc.perform(get("/budget/byId/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser("AccountUser")
    void getAllByAccountId_shouldFindAllBudgetsByGivenAccountId() throws Exception {
        String jsonResponse = """
                [
                    {
                    "id": 1,
                    "title": "Budget test 1",
                    "type": "OTHERS",
                    "upperLimit": 100,
                    "usedLimit": 0
                    },
                    {
                    "id": 2,
                    "title": "Budget test 2",
                    "type": "SHOPPING",
                    "upperLimit": 500,
                    "usedLimit": 0
                    }
                ]
                """;
        when(budgetService.getAllByAccountId(1L)).thenReturn(budgets);

        mockMvc.perform(get("/budget/byAccount/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    @WithMockUser("AccountUser")
    void getAllByAccountId_shouldThrowExceptionWhenGivenInvalidAccountId() throws Exception {

        when(budgetService.getAllByAccountId(99L)).thenThrow(BudgetNotFoundException.class);

        mockMvc.perform(get("/budget/byAccount/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createNewBudgetLimit_shouldCreateNewBudget() throws Exception {
        //given
        BudgetLimitDto budgetDto = new BudgetLimitDto();
        budgetDto.setAccount(account);
        budgetDto.setTitle("Budget test 2");
        budgetDto.setType(TransactionType.SHOPPING);
        budgetDto.setLimit(new BigDecimal(500));

        String json = """
                    {
                        "account": {
                            "id": 1
                            },
                        "type": "SHOPPING",
                        "limit": 500,
                        "title": "Budget test 2"
                    }
                """;
        //when
        when(budgetService.addNew(budgetDto)).thenReturn(budgets.get(1));
        //then
        mockMvc.perform(post("/budget/new")
                        .with(SecurityMockMvcRequestPostProcessors.user("AccountUser"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void createNewBudgetLimit_shouldNotCreateWhenNoAuthorisation() throws Exception {
        //given
        String json = """
                    {
                        "account": {
                            "id": 1
                            },
                        "type": "SHOPPING",
                        "limit": 500,
                        "title": "Budget test 2"
                    }
                """;
        //when
        //then
        mockMvc.perform(post("/budget/new")
                        .with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createNewBudgetLimit_shouldNotCreateWhenInvalidInputData() throws Exception {
        //given
        String json = """
                    {
                        "type": "",
                        "limit": 0,
                        "title": ""
                    }
                """;
        //when
        //then
        mockMvc.perform(post("/budget/new")
                        .with(SecurityMockMvcRequestPostProcessors.user("AccountUser"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser("AccountUser")
    void getStatisticsByAccountId_shouldReturnStatus200() throws Exception {
        //given
        BudgetLimitResponseDto dto = new BudgetLimitResponseDto();
        dto.setTitle(budgets.get(0).getTitle());
        dto.setType(budgets.get(0).getType());
        dto.setUpperLimit(budgets.get(0).getUpperLimit());
        dto.setUsedLimit(budgets.get(0).getUsedLimit());

        List<String> result = List.of(dto.showStats());
        //when
        when(budgetService.getStatisticsById(1L)).thenReturn(result);
        //then
        mockMvc.perform(get("/budget/statistics/1"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser("AccountUser")
    void getStatisticsByAccountId_shouldThrowExceptionWhenIdNotFound() throws Exception {
        //given
        //when
        when(budgetService.getStatisticsById(99L)).thenThrow(BudgetNotFoundException.class);
        //then
        mockMvc.perform(get("/budget/statistics/99"))
                .andExpect(status().isNotFound());
    }
}