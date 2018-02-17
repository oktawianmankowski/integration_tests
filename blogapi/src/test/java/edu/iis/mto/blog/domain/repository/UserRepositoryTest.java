package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("tom");
        user.setLastName("doe");
        user.setEmail("tomdoe@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
        repository.deleteAllInBatch();
        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(0));
    }


    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        repository.deleteAllInBatch();
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
    public void returnTrueWhenFindUserIfFirstNameIsProperly() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                "tom", "doe...", "tomdoe@domain.com...");

        Assert.assertThat(users.get(0).getFirstName(), Matchers.is(user.getFirstName()));
    }

    @Test
    public void returnTrueWhenFindUserIfLastNameIsProperly() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                "tom...", "doe", "tomdoe@domain.com...");

        Assert.assertThat(users.get(0).getFirstName(), Matchers.is(user.getFirstName()));
    }

}
