package simple.security.twitter.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import simple.security.twitter.services.UserService;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final UserService userService;

    public AdminUserConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        userService.createDefaultAdmin();
    }

}
