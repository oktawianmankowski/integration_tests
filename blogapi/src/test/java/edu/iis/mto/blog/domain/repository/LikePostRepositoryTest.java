package edu.iis.mto.blog.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private LikePostRepository repository;
    @Autowired
    private BlogPostRepository blogPostRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;
    private BlogPost blogPost;
    private LikePost likePost;

    @Before
    public void setUp() {
        List<LikePost> likePosts = new ArrayList<>();

        likePosts.add(likePost);

        user = new User();
        user.setFirstName("Jan");
        user.setLastName("test");
        user.setEmail("john@test.com");
        user.setAccountStatus(AccountStatus.NEW);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("Post");
        blogPost.setLikes(likePosts);

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);

    }

    @Test
    public void noLikedPostFound() {
        int expected = 0;
        int actual = repository.findAll().size();
        Assert.assertThat(actual, Matchers.is(expected));

    }

    @Test
    public void oneLikedPostTestHasBeenFound() {
        int expected = 1;
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        LikePost persistedLikePost = entityManager.persist(likePost);
        List<LikePost> likePosts = repository.findAll();
        Assert.assertThat(likePosts.size(), Matchers.is(expected));
        Assert.assertThat(likePosts.get(0).getPost(), Matchers.is(persistedLikePost.getPost()));
    }

    @Test
    public void findLikePostByUserAndBlogPostTest() {
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        repository.save(likePost);
        Optional<LikePost> likePosts = repository.findByUserAndPost(user, blogPost);
        Assert.assertThat(likePosts.get().getId(), Matchers.is(likePost.getId()));
    }
}
