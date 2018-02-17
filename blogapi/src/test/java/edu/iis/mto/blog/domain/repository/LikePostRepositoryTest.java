package edu.iis.mto.blog.domain.repository;

import static org.junit.Assert.assertEquals;

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

	@Autowired
	private LikePostRepository postRepository;

	private static final String FIRST_NAME = "Jan";

	private static final String LAST_NAME = "Kowalski";

	private static final String EMAIL = "jan@kowalski.com";

	private LikePost likePost;

	private BlogPost blogPost;

	private User user;

	@Before
	public void setUp() {
		user = new User();
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setEmail(EMAIL);
		user.setAccountStatus(AccountStatus.NEW);

		blogPost = new BlogPost();
		blogPost.setUser(user);
		blogPost.setEntry("Blog Entry");

		List<LikePost> posts = new ArrayList<>();
		posts.add(likePost);
		blogPost.setLikes(posts);

		likePost = new LikePost();
		likePost.setUser(user);
		likePost.setPost(blogPost);

		userRepository.deleteAllInBatch();
	}

	@Test
	public void shouldNotFindPostsIfRepositoryIsEmpty() {
		assertEquals(0, postRepository.findAll().size());
	}

}
