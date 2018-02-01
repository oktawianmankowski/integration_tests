package edu.iis.mto.blog.domain.repository;

import java.util.ArrayList;
import java.util.List;

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
    public void test() {

    }

}
