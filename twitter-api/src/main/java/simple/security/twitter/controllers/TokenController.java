package simple.security.twitter.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import simple.security.twitter.dtos.LoginRequestDto;
import simple.security.twitter.dtos.LoginResponseDto;
import simple.security.twitter.services.TokenService;

@RestController
public class TokenController {

    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        logger.info("Received login request for user: {}", loginRequestDto.username());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(tokenService.login(loginRequestDto));
    }

}
