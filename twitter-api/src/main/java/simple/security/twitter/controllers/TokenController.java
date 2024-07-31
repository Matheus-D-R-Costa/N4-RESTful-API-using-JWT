package simple.security.twitter.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import simple.security.twitter.dtos.LoginRequestDto;
import simple.security.twitter.dtos.LoginResponseDto;

@RestController
public class TokenController {

    private final JwtEncoder encoder;

    public TokenController(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {

    }
}
