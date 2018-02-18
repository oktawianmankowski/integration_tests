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
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    DataMapper dataMapper;

    @Autowired
    BlogService blogService;

    @MockBean
    BlogPostRepository blogPostRepository;

    @MockBean
    LikePostRepository likePostRepository;

    User user;
    BlogPost blogPost;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("test");
        user.setEmail("john@test.com");
        user.setAccountStatus(AccountStatus.CONFIRMED);
        user.setId(2l);

        User temporaryuser = new User();
        temporaryuser.setFirstName("test");
        temporaryuser.setLastName("test");
        temporaryuser.setEmail("test@test.com");
        temporaryuser.setAccountStatus(AccountStatus.CONFIRMED);
        temporaryuser.setId(4l);

        blogPost = new BlogPost();
        blogPost.setUser(temporaryuser);
        blogPost.setEntry("TEST");
        blogPost.setId(3l);
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void userWithConfirmedStatusLikePost() {
        boolean expected = true;
        Mockito.when(userRepository.findOne(user.getId())).thenReturn(user);
        Mockito.when(blogPostRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Mockito.when(likePostRepository.findByUserAndPost(user, blogPost)).thenReturn(Optional.empty());
        boolean actual = blogService.addLikeToPost(user.getId(), blogPost.getId());
        Assert.assertThat(actual, Matchers.is(expected));
    }

    @Test(expected = DomainError.class)
    public void userWithNewStatusLikePost() {
        user.setAccountStatus(AccountStatus.NEW);
        Mockito.when(userRepository.findOne(user.getId())).thenReturn(user);
        blogService.addLikeToPost(user.getId(), blogPost.getId());

    }

}
