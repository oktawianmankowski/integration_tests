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

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("jan@gmail.com");
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
    public void nonExistingUserSearchTest() {
        firstName = "Nieistniejacy";
        lastName = "Uzytkownik";
        email = "nieistniejacy@email.com";
        repository.save(user);

        List<User> foundUserList = repository
                .findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(firstName, lastName,
                        email);
        Assert.assertThat(foundUserList, Matchers.hasSize(0));
    }

    @Test
    public void correctFirstNameWithDifferentOtherValuesTest() {
        lastName = "Inne";
        email = "inny@email.com";
        repository.save(user);
        List<User> foundUserList = repository
                .findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("Jan", lastName, email);
        Assert.assertThat(foundUserList, Matchers.hasSize(1));
        Assert.assertThat(foundUserList.get(0).getFirstName(), Matchers.is(user.getFirstName()));
    }

    @Test
    public void correctLastNameWithDifferentOtherValuesTest() {
        firstName = "Inne";
        email = "inny@gmail.com";
        repository.save(user);
        List<User> foundUserList = repository
                .findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(firstName,
                        user.getLastName(), email);
        Assert.assertThat(foundUserList, Matchers.hasSize(1));
        Assert.assertThat(foundUserList.get(0).getLastName(), Matchers.is(user.getLastName()));
    }

    @Test
    public void correctEmailNameWithDifferentOtherValuesTest() {
        firstName = "InneImie";
        lastName = "InneNazwisko";
        repository.save(user);
        List<User> foundUserList = repository
                .findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(firstName, lastName,
                        user.getEmail());
        Assert.assertThat(foundUserList, Matchers.hasSize(1));
        Assert.assertThat(foundUserList.get(0).getEmail(), Matchers.is(user.getEmail()));
    }

}
