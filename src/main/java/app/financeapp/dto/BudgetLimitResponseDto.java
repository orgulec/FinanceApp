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
        double percent = usedLimit.doubleValue()/(upperLimit.doubleValue())*1000d;
        percent = Math.round(percent)/10d;
        return title +
                " (" + type + ") " +
                " - Used limit: " + usedLimit.doubleValue() + " / " + upperLimit.doubleValue() + " (" + percent + "%)";
    }
}
