package app.financeapp.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class LoginDto {
    @Column(name = "LOGIN", unique = true, length = 128, nullable = false)
    private String login;
    @Column(name = "PASSWORD", unique = true, length = 128, nullable = false)
    private String password;
}
