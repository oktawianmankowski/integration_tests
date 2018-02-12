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
    private String firstName;
    private String lastName;
    private String email;
    private String test;

    @Before
    public void setUp() {
        firstName = "Jan";
        lastName = "Kowalski";
        email = "john@domain.com";
        test = "test";
        user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
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
    public void shouldFindUserWhenFirstNameIsCorrect() {
        repository.save(user);
        List<User> users = repository
                .findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(firstName, test, test);
        Assert.assertThat(users.get(0).getFirstName(), Matchers.is(user.getFirstName()));
    }

    @Test
    public void shouldFindUserWhenLastNameIsCorrect() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(test,
                lastName, test);
        Assert.assertThat(users.get(0).getLastName(), Matchers.is(user.getLastName()));
    }

    @Test
    public void shouldFindUserWhenEmailIsCorrect() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(test,
                test, email);
        Assert.assertThat(users.get(0).getEmail(), Matchers.is(user.getEmail()));
    }

    @Test
    public void shouldFindNoUserWhenDataIsNotCorrect() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(test,
                test, test);
        Assert.assertThat(users, Matchers.hasSize(0));
    }

}
