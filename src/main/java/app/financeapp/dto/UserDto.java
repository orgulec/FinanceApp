package app.financeapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;
}
