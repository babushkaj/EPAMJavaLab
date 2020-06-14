package com.epam.lab.repository;

import com.epam.lab.configuration.RepositoryConfig;
import com.epam.lab.exception.UserNotFoundException;
import com.epam.lab.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RepositoryConfig.class})
@ActiveProfiles("test")
@Transactional
public class UserRepositoryImplTest {

    private static final long USER_ID = 1L;
    private static final long NONEXISTENT_USER_ID = 4L;
    private static final String USER_NAME = "One";
    private static final String USER_SURNAME = "First";
    private static final String USER_LOGIN = "One";
    private static final String USER_PASSWORD = "First";
    private static final String UPDATED_USER_NAME = "Updated";

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveNewUser() {
        User user = new User();
        user.setName(USER_NAME);
        user.setSurname(USER_SURNAME);
        user.setLogin(USER_LOGIN);
        user.setPassword(USER_PASSWORD);
        long id = userRepository.insert(user);
        Assert.assertEquals(NONEXISTENT_USER_ID, id);
    }

    @Test
    public void shouldSelectOneUserById() {
        User userFromDB = userRepository.findById(USER_ID);
        User expectedUser = new User();
        expectedUser.setId(USER_ID);
        expectedUser.setName(USER_NAME);
        expectedUser.setSurname(USER_SURNAME);
        expectedUser.setLogin(USER_LOGIN);
        expectedUser.setPassword(USER_PASSWORD);
        Assert.assertEquals(userFromDB, expectedUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionNoUserWithSuchId() {
        userRepository.findById(NONEXISTENT_USER_ID);
    }

    @Test
    public void shouldSelectAllUsers() {
        List<User> users = userRepository.findAll(0, 3);
        Assert.assertEquals(3, users.size());
    }

    @Test
    public void shouldUpdateUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setName(UPDATED_USER_NAME);
        user.setSurname(UPDATED_USER_NAME);
        user.setLogin(USER_LOGIN);
        user.setPassword(USER_PASSWORD);
        userRepository.update(user);
        User updatedUser = userRepository.findById(USER_ID);
        Assert.assertEquals(UPDATED_USER_NAME, updatedUser.getName());
        Assert.assertEquals(UPDATED_USER_NAME, updatedUser.getSurname());
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionCannotUpdateUserWithSuchId() {
        User nonexistentUser = new User();
        nonexistentUser.setId(NONEXISTENT_USER_ID);
        nonexistentUser.setName(UPDATED_USER_NAME);
        nonexistentUser.setSurname(UPDATED_USER_NAME);
        nonexistentUser.setLogin(USER_LOGIN);
        nonexistentUser.setPassword(USER_PASSWORD);
        userRepository.update(nonexistentUser);
    }

    @Test
    public void shouldDeleteAuthor() {
        userRepository.delete(USER_ID);
        List<User> users = userRepository.findAll(0, 3);
        Assert.assertEquals(2, users.size());
    }

    @Test
    public void shouldFindUserByLogin() {
        User user = userRepository.findByLogin(USER_LOGIN);
        long userId = user.getId();
        Assert.assertEquals(USER_ID, userId);
    }

    @Test
    public void shouldReturnNullWhenFindUserByLogin() {
        User user = userRepository.findByLogin(UPDATED_USER_NAME);
        Assert.assertNull(user);
    }

    @Test
    public void shouldCountUsers() {
        long count = userRepository.countAll();
        Assert.assertEquals(3, count);
    }

}
