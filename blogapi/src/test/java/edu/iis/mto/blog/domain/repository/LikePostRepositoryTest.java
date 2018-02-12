package edu.iis.mto.blog.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    private LikePostRepository likePostRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;

    private LikePost likePost;
    private BlogPost blogPost;
    private User user;
    private String userFirstName;
    private String userLastName;
    private String userEmail;

    @Before
    public void setUp() {
        userFirstName = "Jan";
        userLastName = "Kowalski";
        userEmail = "john@domain.com";

        user = new User();
        user.setFirstName(userFirstName);
        user.setLastName(userLastName);
        user.setEmail(userEmail);
        user.setAccountStatus(AccountStatus.NEW);

        blogPost = new BlogPost();
        blogPost.setEntry("TEST");
        blogPost.setUser(user);
        List<LikePost> likePostList = new ArrayList<>();
        likePostList.add(likePost);

        likePost = new LikePost();
        likePost.setPost(blogPost);
        likePost.setUser(user);

        userRepository.deleteAllInBatch();
        blogPostRepository.deleteAllInBatch();
        likePostRepository.deleteAllInBatch();
    }

    @Test
    public void shouldReturnLikePostWithIdNotNull() {
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        LikePost persistedLikePost = likePostRepository.save(likePost);
        Assert.assertThat(persistedLikePost.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldReturnLikePostWithModifiedBlogPostEntry() {
        String modifiedBlogPostEntry = "MODIFIED TEST";

        userRepository.save(user);
        blogPostRepository.save(blogPost);
        LikePost persistedLikePost = likePostRepository.save(likePost);

        persistedLikePost.getPost().setEntry(modifiedBlogPostEntry);
        persistedLikePost = likePostRepository.save(persistedLikePost);

        Assert.assertThat(persistedLikePost.getPost().getEntry(), Matchers.equalTo(modifiedBlogPostEntry));
    }

    @Test
    public void shouldReturnLikePostWithCorrectUserAndBlogPost() {
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        likePostRepository.save(likePost);

        Optional<LikePost> likePostOptional = likePostRepository.findByUserAndPost(user, blogPost);
        Assert.assertThat(likePostOptional.get(), Matchers.is(likePost));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionBecauseOfSearchingByIncorrectUser() {
        User otherUser = new User();
        otherUser.setFirstName("Maciej");
        otherUser.setLastName("Nowak");
        otherUser.setEmail("nowak@domain.com");
        otherUser.setAccountStatus(AccountStatus.NEW);

        userRepository.save(otherUser);
        userRepository.save(user);
        blogPostRepository.save(blogPost);
        likePostRepository.save(likePost);

        Optional<LikePost> likePostOptional = likePostRepository.findByUserAndPost(otherUser, blogPost);
        likePostOptional.get();
    }
}
