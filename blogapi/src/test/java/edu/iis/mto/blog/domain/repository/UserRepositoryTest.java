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

import static org.hamcrest.CoreMatchers.is;

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
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("jon@interia.com");
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
    public void shouldFindUserByUsernameWhenFirstNameIsCorrect() throws Exception {
        String differentLastName = "Nowak";
        String differentEmail = "nowak@nowak.com";
        repository.save(user);
        List<User> foundUserList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", differentLastName, differentEmail);
        Assert.assertThat(foundUserList, Matchers.hasSize(1));
        Assert.assertThat(foundUserList.get(0).getFirstName(), is(user.getFirstName()));
    }

    @Test
    public void shouldFindUserIfLastNameIsCorrect() throws Exception {
        String differentFirstName = "Zbigniew";
        String differentEmail = "nowak@nowak.com";
        repository.save(user);
        List<User> foundUserList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(differentFirstName, user.getLastName(), differentEmail);
        Assert.assertThat(foundUserList, Matchers.hasSize(1));
        Assert.assertThat(foundUserList.get(0).getFirstName(), is(user.getFirstName()));
    }

    @Test
    public void shouldFindUserWhenEmailIsCorrect() throws Exception {
        String differentFirstName = "Zbigniew";
        String differentLastName = "Nowak";
        repository.save(user);
        List<User> foundUserList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(differentFirstName, differentLastName, user.getEmail());
        Assert.assertThat(foundUserList, Matchers.hasSize(1));
        Assert.assertThat(foundUserList.get(0).getFirstName(), is(user.getFirstName()));
    }

    @Test
    public void shouldNotFindUser() throws Exception {
        String differentFirstName = "Janek";
        String differentLastName = "Nowak";
        String differentEmail = "nowak@nowak.com";
        repository.save(user);

        List<User> foundUserList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(differentFirstName, differentLastName, differentEmail);
        Assert.assertThat(foundUserList, Matchers.hasSize(0));
    }

    @Test
    public void shouldStoreANewUser() {
        User persistedUser = repository.save(user);
        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

}
