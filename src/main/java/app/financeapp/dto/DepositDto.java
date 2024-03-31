package app.financeapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
@Data
@NoArgsConstructor
public class DepositDto {
    @NotNull
    private Long accountId;
    @NotNull
    private BigDecimal balance;

    private ZonedDateTime creationDate;
    @NotNull
    private ZonedDateTime plannedEndDate;
}
