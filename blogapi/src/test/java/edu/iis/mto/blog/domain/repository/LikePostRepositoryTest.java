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
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogRepository;

    @Autowired
    private LikePostRepository postRepository;

    private User user;
    private LikePost likePost;
    private BlogPost blogPost;
    private String firstName;
    private String lastName;
    private String email;

    @Before
    public void setUp() {
        firstName = "Jan";
        lastName = "Kowalski";
        email = "john@domain.com";
        user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAccountStatus(AccountStatus.NEW);
        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("Test");
        List<LikePost> posts = new ArrayList<>();
        posts.add(likePost);
        blogPost.setLikes(posts);
        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);
        userRepository.deleteAllInBatch();
    }

    @Test
    public void shouldFindNoPostsIfRepositoryIsEmpty() {
        List<LikePost> posts = postRepository.findAll();
        Assert.assertThat(posts, Matchers.hasSize(0));
    }

    @Test
    public void shouldStoreANewPost() {
        userRepository.save(user);
        blogRepository.save(blogPost);
        LikePost persistedPost = postRepository.save(likePost);
        Assert.assertThat(persistedPost.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldFindByUserAndPost() {
        userRepository.save(user);
        blogRepository.save(blogPost);
        postRepository.save(likePost);
        Optional<LikePost> found = postRepository.findByUserAndPost(user, blogPost);
        Assert.assertThat(found.isPresent(), Matchers.is(true));
    }

    @Test
    public void shouldNotFindByFalseUserAndPost() {
        userRepository.save(user);
        blogRepository.save(blogPost);
        postRepository.save(likePost);
        User falseUser = new User();
        falseUser.setFirstName("Karol");
        falseUser.setLastName("Nowak");
        falseUser.setEmail("karol@domain.com");
        falseUser.setAccountStatus(AccountStatus.NEW);
        userRepository.save(falseUser);
        Optional<LikePost> found = postRepository.findByUserAndPost(falseUser, blogPost);
        Assert.assertThat(found.isPresent(), Matchers.is(false));
    }

    @Test
    public void shouldNotFindByUserAndFalsePost() {
        userRepository.save(user);
        blogRepository.save(blogPost);
        postRepository.save(likePost);
        BlogPost falseBlogPost = new BlogPost();
        falseBlogPost.setUser(user);
        falseBlogPost.setEntry("False");
        blogRepository.save(falseBlogPost);
        Optional<LikePost> found = postRepository.findByUserAndPost(user, falseBlogPost);
        Assert.assertThat(found.isPresent(), Matchers.is(false));
    }

}
