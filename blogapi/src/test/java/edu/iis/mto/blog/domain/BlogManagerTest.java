package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
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

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.DataMapper;
import edu.iis.mto.blog.services.BlogService;

import java.util.Optional;

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

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void onlyUsersAfterConfirmationCanLikePosts() {
        User firstUser = new User();
        firstUser.setId(7L);
        firstUser.setFirstName("john");
        firstUser.setLastName("doe");
        firstUser.setEmail("john@doe.com");
        firstUser.setAccountStatus(AccountStatus.NEW); // can't like posts

        User secondUser = new User();
        secondUser.setId(17L);
        secondUser.setFirstName("Tom");
        secondUser.setLastName("Brown");
        secondUser.setEmail("tom@brown.com");
        secondUser.setAccountStatus(AccountStatus.CONFIRMED); // can like posts

        BlogPost blogPost = new BlogPost();
        blogPost.setUser(firstUser);
        blogPost.setEntry("Entry");
        blogPost.setId(717L);

        Mockito.when(userRepository.findOne(secondUser.getId())).thenReturn(secondUser);
        Mockito.when(blogPostRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Optional<LikePost> likePost = Optional.empty();
        Mockito.when(likePostRepository.findByUserAndPost(secondUser, blogPost)).thenReturn(likePost);

        Assert.assertThat(blogService.addLikeToPost(secondUser.getId(), blogPost.getId()), Matchers.is(true));
    }

}
