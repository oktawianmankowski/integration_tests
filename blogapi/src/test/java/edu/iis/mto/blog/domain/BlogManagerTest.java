package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.api.request.UserRequest;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class) @SpringBootTest public class BlogManagerTest {

    @MockBean UserRepository userRepository;

    @MockBean BlogPostRepository blogRepository;
    @MockBean LikePostRepository likeRepository;

    @Autowired DataMapper dataMapper;

    @Autowired BlogService blogService;

    User user;
    User userWhoLikesPost;

    BlogPost blogPost;

    @Before public void setUp() {
        user = createUser(30L, "Zbigniew", "", "zbigniew@interia.com", AccountStatus.CONFIRMED);
        userWhoLikesPost = createUser(31L, "Czesław", "Miłosz", "czeslaw.milosz@domain.com", AccountStatus.CONFIRMED);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("New post");
        blogPost.setId(23L);

    }

    @Test public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test public void addingLikeToPostShouldUserWithAccountCONFIRMED() {
        Mockito.when(userRepository.findOne(userWhoLikesPost.getId())).thenReturn(userWhoLikesPost);
        Mockito.when(blogRepository.findOne(blogPost.getId())).thenReturn(blogPost);
        Optional<LikePost> likes = Optional.empty();
        Mockito.when(likeRepository.findByUserAndPost(userWhoLikesPost, blogPost)).thenReturn(likes);
        Assert.assertThat(blogService.addLikeToPost(userWhoLikesPost.getId(), blogPost.getId()),
                Matchers.equalTo(true));
    }

    private User createUser(Long id, String name, String surname, String email, AccountStatus accountStatus) {
        User newUser = new User();
        newUser.setId(id);
        newUser.setFirstName(name);
        newUser.setLastName(surname);
        newUser.setEmail(email);
        newUser.setAccountStatus(accountStatus);

        return newUser;
    }
}
