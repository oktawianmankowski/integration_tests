package edu.iis.mto.blog.domain;

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
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

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

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test (expected = DomainError.class)
    public void shouldThrowErrorForNewUser() {
        User user = createUser();
        User user2 = createUser2();

        Mockito.when(userRepository.findOne(1L)).thenReturn(user);
        Mockito.when(userRepository.findOne(2L)).thenReturn(user2);
        Mockito.when(blogPostRepository.findOne(1L)).thenReturn(createBlogPost(user));

        blogService.addLikeToPost(2L, 1L);
    }

    @Test (expected = DomainError.class)
    public void shouldThrowErrorForTheSameUser() {
        User user = createUser();

        Mockito.when(userRepository.findOne(1L)).thenReturn(user);
        Mockito.when(blogPostRepository.findOne(1L)).thenReturn(createBlogPost(user));

        blogService.addLikeToPost(1L, 1L);
    }

    @Test
    public void shouldAddLike() {
        User user = createUser();
        User user2 = createUser2();
        BlogPost blogPost = createBlogPost(user2);

        Mockito.when(userRepository.findOne(1L)).thenReturn(user);
        Mockito.when(userRepository.findOne(2L)).thenReturn(user2);
        Mockito.when(blogPostRepository.findOne(1L)).thenReturn(blogPost);
        Mockito.when(likePostRepository.findByUserAndPost(user, blogPost)).thenReturn(Optional.<LikePost>empty());

        blogService.addLikeToPost(1L, 1L);

        ArgumentCaptor<LikePost> likePostParam = ArgumentCaptor.forClass(LikePost.class);
        Mockito.verify(likePostRepository).save(likePostParam.capture());
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john_d@domain.com");
        user.setAccountStatus(AccountStatus.CONFIRMED);
        return user;
    }

    private User createUser2() {
        User user = new User();
        user.setId(2L);
        user.setFirstName("Tom");
        user.setLastName("Jones");
        user.setEmail("t_jones@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        return user;
    }

    private BlogPost createBlogPost(User user) {
        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
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
