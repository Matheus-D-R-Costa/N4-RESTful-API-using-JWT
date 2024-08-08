package simple.security.twitter.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import simple.security.twitter.dtos.FeedDto;
import simple.security.twitter.dtos.FeedItemDto;
import simple.security.twitter.dtos.TweetDto;
import simple.security.twitter.repositories.TweetRepository;
import simple.security.twitter.services.TweetService;

@RestController
public class TweetController {

    private final TweetService tweetService;
    private final TweetRepository tweetRepository;

    public TweetController(TweetService tweetService, TweetRepository tweetRepository) {
        this.tweetService = tweetService;
        this.tweetRepository = tweetRepository;
    }

    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        var tweets = tweetRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
                .map(tweet -> new FeedItemDto(tweet.getId(), tweet.getContent(), tweet.getUser().getUsername()));

        return ResponseEntity.ok(new FeedDto(tweets.getContent(), page, pageSize, tweets.getTotalPages(), tweets.getTotalElements()));
    }

    @PostMapping("/tweets")
    public ResponseEntity<Void> create(@RequestBody @Valid TweetDto dto, JwtAuthenticationToken token) {
        tweetService.create(dto, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, JwtAuthenticationToken token) {
        tweetService.delete(id, token);
        return ResponseEntity.noContent().build();
    }

}
