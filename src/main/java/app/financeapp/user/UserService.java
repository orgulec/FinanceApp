package app.financeapp.user;

import app.financeapp.utils.enums.ExceptionMsg;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
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
