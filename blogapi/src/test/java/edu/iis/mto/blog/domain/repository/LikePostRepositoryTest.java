package edu.iis.mto.blog.domain.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	private BlogPostRepository blogRepository;

	@Autowired
	private LikePostRepository likeRepository;

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
		blogRepository.deleteAllInBatch();
		likeRepository.deleteAllInBatch();
	}

	@Test
	public void shouldNotFindPostsIfRepositoryIsEmpty() {
		assertEquals(0, likeRepository.findAll().size());
	}

	@Test
	public void shouldReturnPostByCorrectUserAndBlogPost() {
		userRepository.save(user);
		blogRepository.save(blogPost);
		likeRepository.save(likePost);

		Optional<LikePost> postOpt = likeRepository.findByUserAndPost(user, blogPost);
		assertTrue(postOpt.isPresent());
		assertEquals(likePost, postOpt.get());
	}

	@Test
	public void shouldStoreANewPost() {
		userRepository.save(user);
		blogRepository.save(blogPost);
		LikePost persistedPost = likeRepository.save(likePost);
		assertNotNull(persistedPost.getId());
	}

	@Test
	public void shouldNotReturnPostByIncorrectUser() {
		userRepository.save(user);
		blogRepository.save(blogPost);
		likeRepository.save(likePost);

		User otherUser = new User();
		otherUser.setFirstName("Adam");
		otherUser.setLastName("Nowak");
		otherUser.setEmail("adam@nowak.com");
		otherUser.setAccountStatus(AccountStatus.NEW);
		userRepository.save(otherUser);

		Optional<LikePost> postOpt = likeRepository.findByUserAndPost(otherUser, blogPost);
		assertFalse(postOpt.isPresent());
	}

	@Test
	public void shouldNotReturnPostByIncorrectPost() {
		userRepository.save(user);
		blogRepository.save(blogPost);
		likeRepository.save(likePost);

		BlogPost incorrectBlogPost = new BlogPost();
		incorrectBlogPost.setUser(user);
		incorrectBlogPost.setEntry("Incorrect Post");
		blogRepository.save(incorrectBlogPost);

		Optional<LikePost> postOpt = likeRepository.findByUserAndPost(user, incorrectBlogPost);
		assertFalse(postOpt.isPresent());
	}

}
