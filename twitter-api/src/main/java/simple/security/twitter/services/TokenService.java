package simple.security.twitter.services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import simple.security.twitter.dtos.login.LoginRequestDto;
import simple.security.twitter.dtos.login.LoginResponseDto;
import simple.security.twitter.models.RoleModel;
import simple.security.twitter.models.UserModel;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public TokenService(UserService userService, BCryptPasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        UserModel user = userService.findByUsername(loginRequestDto.username());
        if (user == null) {
            throw new BadCredentialsException("User or password is invalid!");
        }
        if (!user.isLoginCorrect(loginRequestDto, passwordEncoder)) {
            throw new BadCredentialsException("user or password is invalid!");
        }

        var expiresIn = 300L;
        var claims = createJwtClaims(user.getId(), expiresIn);

        var jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDto(jwt, expiresIn);
    }

    private JwtClaimsSet createJwtClaims(UUID userId, Long expiresIn) {
        var now = Instant.now();
        var scopes = userService.findById(userId).getRoles()
                .stream()
                .map(RoleModel::getName)
                .map(Enum::toString)
                .collect(Collectors.toList());

        return JwtClaimsSet.builder()
                .issuer("twitter-api")
                .subject(userId.toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("roles", scopes)
                .build();

    }

}
