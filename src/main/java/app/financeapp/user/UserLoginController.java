package app.financeapp.user;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class UserLoginController {

    private final AuthenticationManager authenticationManager;

    /**
     * Security log into application
//     * @param dto with String login (AccountUser) and String password (pass)
     * @return HttpStatus 202 if successfully logged in
     */
    @PostMapping("/log")
//    public ResponseEntity<String> login(@Valid @RequestBody LoginDto dto){
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getLogin(),dto.getPassword()));
//        return new ResponseEntity<>("User successfully logged in.", HttpStatus.ACCEPTED);

    public ResponseEntity<String> login(@NotNull @RequestParam String login, String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login,password));
        return new ResponseEntity<>("User successfully logged in.", HttpStatus.ACCEPTED);
    }



}
