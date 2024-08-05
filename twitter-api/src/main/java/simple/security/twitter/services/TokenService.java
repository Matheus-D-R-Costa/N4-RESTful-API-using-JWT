package simple.security.twitter.services;

import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import simple.security.twitter.dtos.LoginRequestDto;
import simple.security.twitter.dtos.LoginResponseDto;
import simple.security.twitter.models.UserModel;
import org.slf4j.Logger;

import java.time.Instant;
import java.util.UUID;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public TokenService(UserService userService, BCryptPasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        logger.info("Attempting to authenticate user: {}", loginRequestDto.username());
        UserModel user = userService.findByUsername(loginRequestDto.username());
        if (user == null) {
            logger.error("User not found: {}", loginRequestDto.username());
            throw new BadCredentialsException("User or password is invalid!");
        }
        if (!user.isLoginCorrect(loginRequestDto, passwordEncoder)) {
            logger.error("Invalid password for user: {}", loginRequestDto.username());
            throw new BadCredentialsException("user or password is invalid!");
        }

        logger.info("User authenticated successfully: {}", loginRequestDto.username());
        var expiresIn = 300L;
        var claims = createJwtClaims(user.getId(), expiresIn);

        var jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        logger.info("Generated JWT for user: {}", loginRequestDto.username());

        return new LoginResponseDto(jwt, expiresIn);

    }

    private JwtClaimsSet createJwtClaims(UUID userId, Long expiresIn) {
        var now = Instant.now();

        return JwtClaimsSet.builder()
                .issuer("twiiter-api")
                .subject(userId.toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

    }


}
