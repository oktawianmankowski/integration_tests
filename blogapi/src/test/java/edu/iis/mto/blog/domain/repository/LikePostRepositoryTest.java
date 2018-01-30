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

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    LikePostRepository repository;


    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;
    private User user;
    private BlogPost blogPost;
    private LikePost likePost;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("joh@domain.com");
        user.setAccountStatus(AccountStatus.NEW);

        blogPost = new BlogPost();
        List<LikePost> likes = new ArrayList<>();
        likes.add(likePost);
        blogPost.setUser(user);
        blogPost.setEntry("Post");
        blogPost.setLikes(likes);

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);

    }


    @Test
    public void shouldFindNoLikesIfRepositoryIsEmpty() {
        List<LikePost> likes = repository.findAll();
        Assert.assertThat(likes, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneLikePost() throws Exception {
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        LikePost persistedLikePost = entityManager.persist(likePost);
        List<LikePost> likes = repository.findAll();
        Assert.assertThat(likes, Matchers.hasSize(1));
        Assert.assertThat(likes.get(0).getPost(), Matchers.equalTo(persistedLikePost.getPost()));
    }

    @Test
    public void shouldFindLikePostByUserAndBlogPost() {

        userRepository.save(user);
        blogPostRepository.save(blogPost);
        repository.save(likePost);
        LikePost found = repository.findByUserAndPost(user, blogPost).orElseThrow(() -> new AssertionError("Should find like post"));

        assertThat(found.getId()).isEqualTo(likePost.getId());
    }

    @Test
    public void testFindByUserAndPostIfUserIsNotCorrect() throws Exception {
        User user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("jon@interia.com");
        user.setAccountStatus(AccountStatus.NEW);

        userRepository.save(this.user);
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        repository.save(likePost);
        Optional<LikePost> found = repository.findByUserAndPost(user, blogPost);
        Assert.assertThat(found.isPresent(), Matchers.equalTo(false));

    }
}
