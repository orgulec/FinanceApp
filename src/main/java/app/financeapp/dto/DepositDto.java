package app.financeapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
@Data
@NoArgsConstructor
public class DepositDto {

    private Long accountId;

    private BigDecimal balance;

    private ZonedDateTime creationDate;

    private ZonedDateTime plannedEndDate;
}
