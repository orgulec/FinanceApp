package app.financeapp.controller;

import app.financeapp.dto.LoginDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;

    /**
     * Security log into application
     * @param dto with String login (AccountUser) and String password (pass)
     * @return HttpStatus 202 if successfully logged in
     */
    @PostMapping
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto dto){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getLogin(),dto.getPassword()));
        return new ResponseEntity<>("User successfully logged in.", HttpStatus.ACCEPTED);
    }



}
