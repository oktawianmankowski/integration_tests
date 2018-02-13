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
    BlogPostRepository blogPostRepository;

    @MockBean
    LikePostRepository likePostRepository;

    @Autowired
    DataMapper dataMapper;

    @Autowired
    BlogService blogService;

    private User user, confirmedUser, newUser;
    private BlogPost blogPost;

    @Before
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);

        confirmedUser = new User();
        confirmedUser.setId(2L);
        confirmedUser.setFirstName("Maciej");
        confirmedUser.setLastName("Nowak");
        confirmedUser.setEmail("nowak@domain.com");
        confirmedUser.setAccountStatus(AccountStatus.CONFIRMED);

        newUser = new User();
        newUser.setId(3L);
        newUser.setFirstName("Jakub");
        newUser.setLastName("Iksiński");
        newUser.setEmail("jacob@domain.com");
        newUser.setAccountStatus(AccountStatus.NEW);

        blogPost = new BlogPost();
        blogPost.setId(4L);
        blogPost.setEntry("TEST");
        blogPost.setUser(user);
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
    public void onlyConfirmedUsersShouldLikePost() {
        Mockito.when(userRepository.findOne(confirmedUser.getId())).thenReturn(confirmedUser);
        Mockito.when(blogPostRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Mockito.when(likePostRepository.findByUserAndPost(confirmedUser, blogPost)).thenReturn(Optional.empty());
        Assert.assertThat(blogService.addLikeToPost(confirmedUser.getId(), blogPost.getId()), Matchers.is(true));
    }

    @Test(expected = DomainError.class)
    public void likePostByNewUserShouldThrowDomainError() {
        Mockito.when(userRepository.findOne(newUser.getId())).thenReturn(newUser);
        Mockito.when(blogPostRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Mockito.when(likePostRepository.findByUserAndPost(newUser, blogPost)).thenReturn(Optional.empty());
        blogService.addLikeToPost(newUser.getId(), blogPost.getId());
    }
}
