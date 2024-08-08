package simple.security.twitter.services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simple.security.twitter.dtos.TweetDto;
import simple.security.twitter.models.TweetModel;
import simple.security.twitter.models.UserModel;
import simple.security.twitter.repositories.TweetRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserService userService;

    public TweetService(TweetRepository tweetRepository, UserService userService) {
        this.tweetRepository = tweetRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public TweetModel findById(Long id) {
        Optional<TweetModel> tweet = tweetRepository.findById(id);
        if (tweet.isEmpty()) throw new IllegalArgumentException("This tweet does not exists!");
        return tweet.get();
    }

    @Transactional
    public void create(TweetDto dto, JwtAuthenticationToken token) {
        UserModel userModel = userService.findById(UUID.fromString(token.getName()));

        TweetModel tweetModel = new TweetModel();
        tweetModel.setUser(userModel);
        tweetModel.setContent(dto.content());

        tweetRepository.save(tweetModel);
    }

    @Transactional
    public void delete(Long id, JwtAuthenticationToken token) {
        var user = userService.findById(UUID.fromString(token.getName()));
        var tweet = findById(id);

        if (user.isAdmin() || tweet.getUser().getId().equals(UUID.fromString(token.getName()))) {
            tweetRepository.delete(tweet);
        } else {
            throw new BadCredentialsException("You cannot delete the tweet of other users!");
        }
    }
}