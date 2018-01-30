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

	@MockBean
	BlogPostRepository blogRepository;

	@MockBean
	LikePostRepository likePostRepository;

	@Autowired
	DataMapper dataMapper;

	@Autowired
	BlogService blogService;

	private User user;
	private User confirmedUser;
	private BlogPost blogPost;

	@Before
	public void setUp() {
		user = new User();
		user.setId(1l);
		user.setFirstName("Dariusz");
		user.setEmail("d@domain.com");
		user.setAccountStatus(AccountStatus.NEW);

		confirmedUser = new User();
		confirmedUser.setId(2l);
		confirmedUser.setFirstName("Grzegorz");
		confirmedUser.setEmail("g@domain.com");
		confirmedUser.setAccountStatus(AccountStatus.CONFIRMED);

		blogPost = new BlogPost();
		blogPost.setUser(user);
		blogPost.setEntry("Entry");
		blogPost.setId(23L);
	}

	@Test
	public void creatingNewUserShouldSetAccountStatusToNEW() {
		blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
		ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
		Mockito.verify(userRepository).save(userParam.capture());
		User user = userParam.getValue();
		Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
	}

	@Test
	public void postShouldBeLikedOnlyByConfirmedUser() {
		Mockito.when(userRepository.findOne(confirmedUser.getId())).thenReturn(confirmedUser);
		Mockito.when(blogRepository.findOne(blogPost.getId())).thenReturn(blogPost);
		Optional<LikePost> likes = Optional.empty();
		Mockito.when(likePostRepository.findByUserAndPost(confirmedUser, blogPost)).thenReturn(likes);

		Assert.assertThat(blogService.addLikeToPost(confirmedUser.getId(), blogPost.getId()), Matchers.is(true));
	}
}
