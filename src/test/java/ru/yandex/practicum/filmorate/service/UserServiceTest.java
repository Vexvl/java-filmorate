package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {

    private final UserService userService;
    User user = new User();
    User user2 = new User();

    User user3 = new User();

    @BeforeEach
    public void create() {
        user.setName("user");
        user.setId(1);
        user.setEmail("email@yandex.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.now());

        user2.setName("user2");
        user2.setId(2);
        user2.setEmail("email@yandex.ru");
        user2.setLogin("login");
        user2.setBirthday(LocalDate.now());
    }

    @Test
    public void testGetUserById() {
        assertNotNull(userService.getUser(1));
    }

    @Test
    void testGetUsers(){
        userService.createUser(user);
        userService.createUser(user2);
        assertEquals(2,userService.getUsers().size());
    }

    @Test
    public void testGetUserByWrongId() {
        final NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> userService.getUser(-1));
        assertEquals("No value present", exception.getMessage());
    }

    @Test
    public void testGetUsersNotEmpty() {
        userService.createUser(user);
        assertNotNull(userService.getUsers());
    }

    @Test
    public void testUpdateUserWithWrongId() {
        final NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> userService.updateUser(userService.getUser(-1)));
        assertEquals("No value present", exception.getMessage());
    }

    @Test
    public void testUpdateUserWithNormalId() {
        userService.createUser(user);
        assertNotNull(userService.updateUser(userService.getUser(1)));
    }

    @Test
    public void testAddFriend() {
        userService.createUser(user);
        userService.createUser(user2);
        userService.addFriend(user.getId(), user2.getId());
        assertNotNull(user.getFriends());
    }

    @Test
    public void testDeleteFriend() {
        userService.createUser(user);
        userService.createUser(user2);
        userService.addFriend(user.getId(), user2.getId());
        userService.deleteFriend(user.getId(), user2.getId());
        assertEquals(0,user.getFriends().size());
    }

    @Test
    public void testGetCommonFriends() {
        userService.createUser(user);
        userService.createUser(user2);
        user3.setName("user");
        user3.setId(3);
        user3.setEmail("email@yandex.ru");
        user3.setLogin("login");
        user3.setBirthday(LocalDate.now());
        userService.createUser(user3);
        userService.addFriend(user.getId(), user3.getId());
        userService.addFriend(user2.getId(), user3.getId());
        List<User> commonFriends = userService.getCommonFriends(user.getId(), user2.getId());
        assertEquals(1, commonFriends.size());
    }
}
