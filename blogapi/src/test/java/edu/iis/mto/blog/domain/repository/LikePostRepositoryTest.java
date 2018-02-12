package edu.iis.mto.blog.domain.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;

    private LikePost likePost;
    private BlogPost blogPost;
    private User user;
    private String userFirstName;
    private String userLastName;
    private String userEmail;

    @Before
    public void setUp() {
        userFirstName = "Jan";
        userLastName = "Kowalski";
        userEmail = "john@domain.com";

        user = new User();
        user.setFirstName(userFirstName);
        user.setLastName(userLastName);
        user.setEmail(userEmail);
        user.setAccountStatus(AccountStatus.NEW);

        blogPost = new BlogPost();
        blogPost.setEntry("TEST");
        blogPost.setUser(user);
        List<LikePost> likePostList = new ArrayList<>();
        likePostList.add(likePost);

        likePost = new LikePost();
        likePost.setPost(blogPost);
        likePost.setUser(user);
    }
}
