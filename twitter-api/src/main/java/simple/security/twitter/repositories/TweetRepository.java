package simple.security.twitter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simple.security.twitter.models.TweetModel;

@Repository
public interface TweetRepository extends JpaRepository<TweetModel, Long> {

}
