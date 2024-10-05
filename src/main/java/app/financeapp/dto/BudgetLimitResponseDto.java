package app.financeapp.dto;

import app.financeapp.transaction.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
public class BudgetLimitResponseDto {

    @NotBlank
    private String title;
    @NotNull
    private TransactionType type;
    @NotNull
    private BigDecimal upperLimit;
    @NotNull
    private BigDecimal usedLimit;


    public String showStats() {
        BigDecimal percent = usedLimit.divide(upperLimit,2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        return title +
                " (" + type + ") " +
                " - Used limit: " + usedLimit.doubleValue() + " / " + upperLimit.doubleValue() + " (" + percent.doubleValue() + "%)";
    }
}
