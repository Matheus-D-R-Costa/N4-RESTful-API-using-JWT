package simple.security.twitter.services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simple.security.twitter.enums.RoleName;
import simple.security.twitter.models.UserModel;
import simple.security.twitter.repositories.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UserModel findByUsername(String username) {
        Optional<UserModel> userModel = userRepository.findByUsername(username);

        if (userModel.isEmpty()) throw new BadCredentialsException("user not found!");

        return userModel.get();

    }

    @Transactional
    public void createDefaultAdmin() {
        var adminRole = roleService.findByName(RoleName.ROLE_ADMIN);

        if (userRepository.existsByUsername("admin")) {
            System.out.println("This system have a admin!");
        } else {
            UserModel admin = new UserModel();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123"));
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
        }
    }

}
