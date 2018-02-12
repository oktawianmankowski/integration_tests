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

import java.util.List;
import java.util.Optional;


/**
 * Created by Justyna on 31.01.2018.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    private User user;
    private User user2;
    private BlogPost blogPost;

    @Autowired
    private LikePostRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setUp() {
        user = entityManager.persist(createUser());
        user2 = entityManager.persist(createUser2());
        blogPost = entityManager.persist(createBlogPost(user));
    }

    @Test
    public void shouldSaveLikePost() {
        LikePost likePost = createLikePost(user, blogPost);
        repository.save(likePost);
        List<LikePost> likePosts = repository.findAll();

        Assert.assertThat(likePosts, Matchers.hasSize(1));
        Assert.assertEquals(likePosts.get(0).getPost().getEntry(), "Post 1");
    }

    @Test
    public void shouldFindByUserAndPost() {
        LikePost likePost = createLikePost(user, blogPost);
        repository.save(likePost);
        Optional<LikePost> optionalResult = repository.findByUserAndPost(user, blogPost);

        LikePost result = optionalResult.orElse(null);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getUser().getEmail(), "john_d@domain.com");
        Assert.assertEquals(result.getPost().getEntry(), "Post 1");
    }

    @Test
    public void shouldNotFindByUserAndPost() {
        LikePost likePost = createLikePost(user, blogPost);
        repository.save(likePost);
        Optional<LikePost> optionalResult = repository.findByUserAndPost(user2, blogPost);

        LikePost result = optionalResult.orElse(null);

        Assert.assertNull(result);
    }

    @Test
    public void shouldModifyLikePost() {
        LikePost likePost = createLikePost(user, blogPost);
        repository.save(likePost);
        LikePost result = repository.findByUserAndPost(user, blogPost).get();

        result.setUser(user2);
        repository.save(result);

        result = repository.findByUserAndPost(user2, blogPost).get();

        Assert.assertEquals(result.getUser().getEmail(), "t_jones@domain.com");
    }

    private User createUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john_d@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        return user;
    }

    private User createUser2() {
        User user = new User();
        user.setFirstName("Tom");
        user.setLastName("Jones");
        user.setEmail("t_jones@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        return user;
    }

    private BlogPost createBlogPost(User user) {
        BlogPost blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("Post 1");
        return blogPost;
    }

    private LikePost createLikePost(User user, BlogPost post) {
        LikePost likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(post);
        return likePost;
    }
}