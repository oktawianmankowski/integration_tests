package edu.iis.mto.blog.domain.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

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
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

	private static final String FIRST_NAME = "Jan";
	private static final String LAST_NAME = "Kowalski";
	private static final String EMAIL = "jan@kowalski.com";

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository repository;

	private User user;

	@Before
	public void setUp() {
		user = new User();
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setEmail(EMAIL);
		user.setAccountStatus(AccountStatus.NEW);
		repository.deleteAllInBatch();
	}

	@Test
	public void shouldFindNoUsersIfRepositoryIsEmpty() {

		List<User> users = repository.findAll();

		Assert.assertThat(users, Matchers.hasSize(0));
	}

	@Test
	public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
		User persistedUser = entityManager.persist(user);
		List<User> users = repository.findAll();

		Assert.assertThat(users, Matchers.hasSize(1));
		Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
	}

	@Test
	public void shouldStoreANewUser() {

		User persistedUser = repository.save(user);

		Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
	}

	@Test
	public void shouldFindUserByCorrectFirstName() {
		repository.save(user);
		List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
				FIRST_NAME, LAST_NAME, EMAIL);

		assertEquals(1, users.size());
		assertEquals(FIRST_NAME, users.get(0).getFirstName());
	}

	@Test
	public void shouldFindUserByCorrectLastName() {
		repository.save(user);
		List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
				FIRST_NAME, LAST_NAME, EMAIL);

		assertEquals(1, users.size());
		assertEquals(LAST_NAME, users.get(0).getLastName());
	}

	@Test
	public void shouldFindUserByCorrectEmail() {
		repository.save(user);
		List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
				FIRST_NAME, LAST_NAME, EMAIL);

		assertEquals(1, users.size());
		assertEquals(EMAIL, users.get(0).getEmail());
	}

	@Test
	public void shouldNotFindIncorrectUser() {
		repository.save(user);
		List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
				"Adam", "Nowak", "adam@nowak.com");

		assertEquals(0, users.size());
	}

}
