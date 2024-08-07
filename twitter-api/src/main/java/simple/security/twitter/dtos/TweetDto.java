package simple.security.twitter.dtos;

import jakarta.validation.constraints.NotBlank;

public record TweetDto(@NotBlank String content) {
}
