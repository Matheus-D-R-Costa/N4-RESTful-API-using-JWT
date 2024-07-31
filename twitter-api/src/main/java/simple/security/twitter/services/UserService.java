package simple.security.twitter.services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simple.security.twitter.dtos.LoginRequestDto;
import simple.security.twitter.models.UserModel;
import simple.security.twitter.repositories.UserRepository;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional(readOnly = true)
    public UserModel findByUsername(LoginRequestDto loginRequestDto) {
        Optional<UserModel> userModel = userRepository.findByUsername(loginRequestDto.username());

        if (userModel.isEmpty() || !userModel.get().isLoginCorrect(loginRequestDto, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("user or password is invalid!");
        }

        Instant now = Instant.now();
        Long expiresIn = 300L;

        var claims = JwtClaimsSet.builder()
                .issuer("Twitter-api")
                .subject(userModel.get().getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        var jwtValue = "";

        //TODO: Refatorar esse método.
    }

}
