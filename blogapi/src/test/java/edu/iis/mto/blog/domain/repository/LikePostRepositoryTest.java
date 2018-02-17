package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Lukasz on 2018-02-17.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private LikePostRepository likePostRepository;

    @Test
    public void findByUserAndPost() {
        User user = new User();
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("Jan@Nowak.pl");
        user.setAccountStatus(AccountStatus.NEW);

        BlogPost blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("Entry");

        List<LikePost> likePosts = new ArrayList<>();
        blogPost.setLikes(likePosts);

        LikePost likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);
        likePosts.add(likePost);

        userRepository.save(user);
        blogPostRepository.save(blogPost);
        likePostRepository.save(likePost);
        Optional<LikePost> foundLikePosts = likePostRepository.findByUserAndPost(user, blogPost);
        Assert.assertThat(foundLikePosts.isPresent(), Matchers.is(true));
    }

}