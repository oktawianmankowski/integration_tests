package edu.iis.mto.blog.domain.repository;

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

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

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
    public void shouldReturnUserWithCorrectFirstName() {
        repository.save(user);
        List<User> userList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                userFirstName, "Nowak", "nowak@domain.com");
        Assert.assertThat(userList.get(0).getFirstName(), Matchers.equalTo(userFirstName));
    }

    @Test
    public void shouldReturnUserWithCorrectLastName() {
        repository.save(user);
        List<User> userList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                "Maciej", userLastName, "m@domain.com");
        Assert.assertThat(userList.get(0).getLastName(), Matchers.equalTo(userLastName));
    }

    @Test
    public void shouldReturnUserWithCorrectEmail() {
        repository.save(user);
        List<User> userList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                "Maciej", "Nowak", userEmail);
        Assert.assertThat(userList.get(0).getEmail(), Matchers.equalTo(userEmail));
    }

    @Test
    public void shouldNotReturnUser() {
        repository.save(user);
        List<User> userList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                "Maciej", "Nowak", "nowak@domain.com");
        Assert.assertThat(userList.size(), Matchers.is(0));
    }
}
