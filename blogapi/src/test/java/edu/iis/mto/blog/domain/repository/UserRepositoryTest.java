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
    private final String FAKENAME = "FAKENAME";
    private final String FAKELASTNAME = "FAKELASTNAME";
    private final String FAKEEMAIL = "FAKEEMAIL@fekemail.com";

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("test");
        user.setEmail("john@test.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(1));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(2));
        Assert.assertThat(users.get(1).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

    @Test
    public void userNotFound() {
        int expected = 0;
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                FAKENAME, FAKELASTNAME, FAKEEMAIL);
        Assert.assertThat(users.size(), Matchers.is(expected));
    }

    @Test
    public void userFoundByEmail() {
        repository.save(user);
        int expected = 1;
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                FAKENAME, FAKELASTNAME, user.getEmail());
        Assert.assertThat(users, Matchers.hasSize(expected));
        Assert.assertThat(users.get(0).getFirstName(), Matchers.is(user.getFirstName()));
        Assert.assertThat(users.get(0).getLastName(), Matchers.is(user.getLastName()));
    }

    @Test
    public void userFoundByFirstName() {
        repository.save(user);
        int expected = 1;
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                user.getFirstName(), FAKELASTNAME, FAKEEMAIL);
        Assert.assertThat(users, Matchers.hasSize(expected));
        Assert.assertThat(users.get(0).getEmail(), Matchers.is(user.getEmail()));
        Assert.assertThat(users.get(0).getLastName(), Matchers.is(user.getLastName()));
    }

}
