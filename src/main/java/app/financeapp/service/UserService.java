package app.financeapp.service;

import app.financeapp.model.UserModel;
import app.financeapp.model.enums.ExceptionMsg;
import app.financeapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
public class UserService {
    private final UserRepository userRepository;

    public UserModel getUserById(Long id){
        Optional<UserModel> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new EntityNotFoundException(ExceptionMsg.USER_NOT_FOUND.toString());
        }
        return user.get();
    }

}
