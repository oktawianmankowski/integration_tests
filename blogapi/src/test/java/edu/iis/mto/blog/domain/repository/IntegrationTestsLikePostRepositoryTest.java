package edu.iis.mto.blog.domain.repository;

import static org.junit.Assert.assertTrue;

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
public class IntegrationTestsLikePostRepositoryTest {

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
    List<LikePost> likedPosts;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("jan@gmail.com");
        user.setAccountStatus(AccountStatus.NEW);

        blogPost = new BlogPost();
        likedPosts = new ArrayList<>();
        likedPosts.add(likePost);
        blogPost.setUser(user);
        blogPost.setEntry("Post");
        blogPost.setLikes(likedPosts);

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);

    }

    @Test
    public void oneLikeTest() {
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        LikePost persistedLikePost = entityManager.persist(likePost);
        likedPosts = repository.findAll();
        Assert.assertThat(likedPosts, Matchers.hasSize(1));
        Assert.assertThat(likedPosts.get(0).getPost(), Matchers.equalTo(persistedLikePost.getPost()));
    }

    @Test
    public void likeFoundByUserAndPostTest() {
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        repository.save(likePost);
        Optional<LikePost> found = repository.findByUserAndPost(user, blogPost);
        assertTrue(found.get().getId() == likePost.getId());
    }

    @Test
    public void noLikedPostsTest() {
        likedPosts = repository.findAll();
        Assert.assertThat(likedPosts, Matchers.hasSize(0));
    }
}
