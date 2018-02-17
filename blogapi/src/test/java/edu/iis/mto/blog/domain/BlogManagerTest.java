package edu.iis.mto.blog.domain;

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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Optional;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
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
	BlogPostRepository blogPostRepository;

	@MockBean
	UserRepository userRepository;

	@MockBean
	LikePostRepository likePostRepository;

	@Autowired
	DataMapper dataMapper;

	@Autowired
	BlogService blogService;

	private BlogPost blogPost;

	@Before
	public void setUp() {
		User user = new User();
		user.setId(1L);
		user.setFirstName("Jan");
		user.setLastName("Kowalski");
		user.setEmail("jan@kowalski.com");
		user.setAccountStatus(AccountStatus.NEW);

		blogPost = new BlogPost();
		blogPost.setId(1L);
		blogPost.setEntry("Blog Entry");
		blogPost.setUser(user);
		doReturn(blogPost).when(blogPostRepository).findOne(blogPost.getId());
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
	public void canConfirmedUserLikePost() {
		User confirmedUser = new User();
		confirmedUser.setId(2L);
		confirmedUser.setFirstName("Confirmed");
		confirmedUser.setLastName("user");
		confirmedUser.setEmail("confirmed@user.com");
		confirmedUser.setAccountStatus(AccountStatus.CONFIRMED);
		doReturn(confirmedUser).when(userRepository).findOne(confirmedUser.getId());
		doReturn(Optional.empty()).when(likePostRepository).findByUserAndPost(confirmedUser, blogPost);

		assertTrue(blogService.addLikeToPost(confirmedUser.getId(), blogPost.getId()));
	}

	@Test(expected = DomainError.class)
	public void notConfirmedUserCannotLikePost() {
		User confirmedUser = new User();
		confirmedUser.setId(2L);
		confirmedUser.setFirstName("notconfirmed");
		confirmedUser.setLastName("user");
		confirmedUser.setEmail("notconfirmed@user.com");
		confirmedUser.setAccountStatus(AccountStatus.NEW);
		doReturn(confirmedUser).when(userRepository).findOne(confirmedUser.getId());
		doReturn(Optional.empty()).when(likePostRepository).findByUserAndPost(confirmedUser, blogPost);

		blogService.addLikeToPost(confirmedUser.getId(), blogPost.getId());
	}

}
