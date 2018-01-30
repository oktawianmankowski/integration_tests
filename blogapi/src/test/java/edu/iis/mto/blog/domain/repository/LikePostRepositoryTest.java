package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;



public class LikePostRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    LikePostRepository repository;

    private User user;
    private BlogPost blogPost;
    private LikePost likePost;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("jon@interia.com");
        user.setAccountStatus(AccountStatus.NEW);

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setId(1L);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("Post");
        blogPost.setId(1L);
        blogPost.setLikes(Arrays.asList(likePost));

    }
}
