package edu.iis.mto.blog.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
	TestEntityManager entityManager;

	@Autowired
	LikePostRepository repository;

	@Autowired
	private BlogPostRepository blogPostRepository;

	@Autowired
	private UserRepository userRepository;
	private User user;
	private String userFirstName;
	private String userLastName;
	private String userEmail;
	private BlogPost blogPost;
	private LikePost likePost;

	@Before
	public void setUp() {
		userFirstName = "Grzegorz";
		userLastName = "Komorowski";
		userEmail = "grzegorz@domain.com";
		user = new User();
		user.setFirstName(userFirstName);
		user.setLastName(userLastName);
		user.setEmail(userEmail);
		user.setAccountStatus(AccountStatus.NEW);

		blogPost = new BlogPost();
		List<LikePost> likesList = new ArrayList<>();
		likesList.add(likePost);
		blogPost.setUser(user);
		blogPost.setEntry("Entry");
		blogPost.setLikes(likesList);

		likePost = new LikePost();
		likePost.setUser(user);
		likePost.setPost(blogPost);

	}

	@Test
	public void shouldStoreANewLikePost() {
		userRepository.save(user);
		blogPostRepository.save(blogPost);
		LikePost persistedLikePost = repository.save(likePost);

		Assert.assertThat(persistedLikePost.getId(), Matchers.notNullValue());
	}

	@Test
	public void modifyingLikePostIsCorrect() {
		String modifiedEntry = "Modified Entry";

		userRepository.save(user);
		blogPostRepository.save(blogPost);
		LikePost persistedLikePost = repository.save(likePost);

		persistedLikePost.getPost().setEntry(modifiedEntry);
		repository.save(persistedLikePost);
		List<LikePost> likePostsList = repository.findAll();

		Assert.assertThat(likePostsList.get(0).getPost().getEntry(), Matchers.is(modifiedEntry));
	}

	@Test
	public void findingLikePostByUserAndPostIsCorrect() {
		userRepository.save(user);
		blogPostRepository.save(blogPost);
		repository.save(likePost);

		Optional<LikePost> likePostList = repository.findByUserAndPost(user, blogPost);
		Assert.assertThat(likePostList.get(), Matchers.is(likePost));
	}
}