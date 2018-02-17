package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class) @DataJpaTest public class LikePostRepositoryTest {

    @Autowired private TestEntityManager entityManager;

    @Autowired private LikePostRepository likePostRepository;

    @Autowired private UserRepository userRepository;

    @Autowired private BlogPostRepository blogPostRepository;

    private User user;
    private LikePost likePost;
    private BlogPost blogPost;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Janek");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);

        userRepository.deleteAllInBatch();

        likePost = new LikePost();
        List<LikePost> likePostList = new ArrayList<>();
        likePostList.add(likePost);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("entry");
        blogPost.setLikes(likePostList);

        likePost.setUser(user);
        likePost.setPost(blogPost);

        userRepository.deleteAllInBatch();
        likePostRepository.deleteAllInBatch();
        blogPostRepository.deleteAllInBatch();
    }

    @Test
    public void shouldSaveNewLikePost() {
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        likePostRepository.save(likePost);

        List<LikePost> likePosts = likePostRepository.findAll();

        Assert.assertThat(likePosts, Matchers.hasSize(1));
        Assert.assertThat(likePosts.get(0).getUser().getEmail(), Matchers.equalTo(user.getEmail()));
    }

    @Test
    public void shouldModifyLikePost() {
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        likePostRepository.save(likePost);

        User userMod = new User();
        userMod.setFirstName("Adam");
        userMod.setLastName("Gora");
        userMod.setEmail("adam@domain.com");
        userMod.setAccountStatus(AccountStatus.NEW);
        userRepository.save(userMod);

        likePost.setUser(userMod);
        List<LikePost> likePosts = likePostRepository.findAll();

        Assert.assertThat(likePosts, Matchers.hasSize(1));
        Assert.assertThat(likePosts.get(0).getUser().getEmail(), Matchers.equalTo(userMod.getEmail()));
    }

    @Test
    public void shouldFindByUserAndPost() {
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        likePostRepository.save(likePost);

        Optional<LikePost> likePostOptional = likePostRepository.findByUserAndPost(user, blogPost);

        Assert.assertTrue(likePostOptional.isPresent());
    }

    @Test
    public void shouldNotFindByUserAndPost() {
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        likePostRepository.save(likePost);

        Optional<LikePost> likePostOptional = likePostRepository.findByUserAndPost(user, null);

        Assert.assertFalse(likePostOptional.isPresent());
    }

}
