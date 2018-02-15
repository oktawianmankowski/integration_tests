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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class) @SpringBootTest public class BlogManagerTest {

    @Autowired DataMapper dataMapper;

    @Autowired BlogService blogService;

    @Autowired private UserRepository userRepository;

    @Autowired private BlogPostRepository blogPostRepository;

    @Autowired private LikePostRepository likePostRepository;

    @Before
    public void init() {
        userRepository.deleteAllInBatch();
        blogPostRepository.deleteAllInBatch();
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        long id = blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        User user = userRepository.findOne(id);
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void addLikeToPostShouldDoneByConfirmedAccounts() {
        User user = new User();
        user.setFirstName("Damian");
        user.setEmail("damian@test.pl");
        user.setAccountStatus(AccountStatus.CONFIRMED);
        userRepository.save(user);

        User userMod = new User();
        userMod.setFirstName("Jan");
        userMod.setLastName("Kowalski");
        userMod.setEmail("john@domain.com");
        userMod.setAccountStatus(AccountStatus.NEW);
        userRepository.save(userMod);

        BlogPost blogPost = new BlogPost();
        blogPost.setEntry("entry");
        blogPost.setUser(userMod);
        blogPostRepository.save(blogPost);

        blogService.addLikeToPost(user.getId(), blogPost.getId());

        List<LikePost> likePostList = likePostRepository.findAll();

        Assert.assertThat(likePostList, Matchers.hasSize(1));

    }
}
