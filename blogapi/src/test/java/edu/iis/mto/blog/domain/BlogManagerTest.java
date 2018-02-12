package edu.iis.mto.blog.domain;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    BlogPostRepository blogRepository;

    @MockBean
    LikePostRepository likeRepository;

    @Autowired
    DataMapper dataMapper;

    @Autowired
    BlogService blogService;

    private User confirmedUser;
    private User newUser;
    private String firstName;
    private String lastName;
    private String email;
    private BlogPost blogPost;
    private Optional<LikePost> likePosts;

    @Before
    public void setUp() {
        firstName = "Jan";
        lastName = "Kowalski";
        email = "john@domain.com";
        confirmedUser = new User();
        confirmedUser.setId(10L);
        confirmedUser.setFirstName(firstName);
        confirmedUser.setLastName(lastName);
        confirmedUser.setEmail(email);
        confirmedUser.setAccountStatus(AccountStatus.CONFIRMED);
        newUser = new User();
        newUser.setId(20L);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setAccountStatus(AccountStatus.NEW);
        blogPost = new BlogPost();
        blogPost.setId(12L);
        blogPost.setEntry("Test");
        likePosts = Optional.empty();
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest(firstName, lastName, email));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void addingLikeToPostByUserWithAccountStatusToConfirmed() {
        blogPost.setUser(newUser);
        Mockito.when(userRepository.findOne(confirmedUser.getId())).thenReturn(confirmedUser);
        Mockito.when(blogRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Mockito.when(likeRepository.findByUserAndPost(confirmedUser, blogPost)).thenReturn(likePosts);
        Assert.assertThat(blogService.addLikeToPost(confirmedUser.getId(), blogPost.getId()), Matchers.is(true));
    }

    @Test(expected = DomainError.class)
    public void notCanAddingLikeToPostByUserWithAccountStatusToNew() {
        blogPost.setUser(confirmedUser);
        Mockito.when(userRepository.findOne(newUser.getId())).thenReturn(newUser);
        Mockito.when(blogRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Mockito.when(likeRepository.findByUserAndPost(newUser, blogPost)).thenReturn(likePosts);
        blogService.addLikeToPost(newUser.getId(), blogPost.getId());
    }

    @Test(expected = DomainError.class)
    public void notCanAddingLikeToOwnPostByUser() {
        blogPost.setUser(confirmedUser);
        Mockito.when(userRepository.findOne(confirmedUser.getId())).thenReturn(confirmedUser);
        Mockito.when(blogRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Mockito.when(likeRepository.findByUserAndPost(confirmedUser, blogPost)).thenReturn(likePosts);
        blogService.addLikeToPost(confirmedUser.getId(), blogPost.getId());
    }

}
