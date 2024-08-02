package simple.security.twitter.services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simple.security.twitter.models.UserModel;
import simple.security.twitter.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserModel findByUsername(String username) {
        Optional<UserModel> userModel = userRepository.findByUsername(username);

        if (userModel.isEmpty()) throw new BadCredentialsException("user not found!");

        return userModel.get();

    }

}
