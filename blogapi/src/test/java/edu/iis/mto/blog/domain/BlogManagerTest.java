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
    @Autowired
    DataMapper dataMapper;
    @Autowired
    BlogService blogService;
    @MockBean
    BlogPostRepository blogRepository;
    @MockBean
    LikePostRepository likeRepository;
    BlogPost blogPost;

    User confirmedUser;
    User newUser;

    @Before
    public void setUp() {
        confirmedUser = new User();
        confirmedUser.setId((long) 11);
        confirmedUser.setFirstName("Jan");
        confirmedUser.setLastName("Pierwszy");
        confirmedUser.setEmail("jan@gmail.com");
        confirmedUser.setAccountStatus(AccountStatus.CONFIRMED);

        newUser = new User();
        newUser.setId((long) 12);
        newUser.setFirstName("Maciek");
        newUser.setLastName("Drugi");
        newUser.setEmail("maciek@gmail.com");
        newUser.setAccountStatus(AccountStatus.NEW);

        blogPost = new BlogPost();
        blogPost.setUser(newUser);
        blogPost.setEntry("New post");
        blogPost.setId((long) 23);

    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
    }

    @Test
    public void userWithCONFIRMEDAccountLikesPostTest() {
        Mockito.when(userRepository.findOne(confirmedUser.getId())).thenReturn(confirmedUser);
        Mockito.when(blogRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Optional<LikePost> likedPosts = Optional.empty();
        Mockito.when(likeRepository.findByUserAndPost(confirmedUser, blogPost)).thenReturn(likedPosts);
        Assert.assertThat(blogService.addLikeToPost(confirmedUser.getId(), blogPost.getId()), Matchers.equalTo(true));
    }

    @Test(expected = DomainError.class)
    public void userWithNEWAccountLikesPostTest() throws Exception {
        Mockito.when(userRepository.findOne(newUser.getId())).thenReturn(newUser);
        Mockito.when(blogRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Optional<LikePost> likedPosts = Optional.empty();
        Mockito.when(likeRepository.findByUserAndPost(newUser, blogPost)).thenReturn(likedPosts);
        Assert.assertThat(blogService.addLikeToPost(newUser.getId(), blogPost.getId()), Matchers.equalTo(true));

    }
}
