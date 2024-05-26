package app.financeapp.dto;

import app.financeapp.model.enums.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BudgetLimitResponseDto {

    private String title;
    private TransactionType type;
    private BigDecimal upperLimit;
    private BigDecimal usedLimit;


    public String showStats() {
        BigDecimal percent = usedLimit.divide(upperLimit).multiply(new BigDecimal(100));
        return title +
                " (" + type + ") " +
                " - Used limit: " + usedLimit.doubleValue() + " / " + upperLimit.doubleValue() + " (" + percent.doubleValue() + "%)";
    }
}
