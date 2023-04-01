package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> userMap = new HashMap<>();

    private long id = 1;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        addUserToList(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        updateUserList(user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<User>(userMap.values());
    }

    private void addUserToList(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(id);
        userMap.put(user.getId(), user);
        log.info("Пользователь с id " + user.getId() + "успешно добавлен");
        id++;
    }

    private User updateUserList(User user) {
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            log.info("Пользователь с id " + user.getId() + "успешно обновлён");
            return user;
        } else {
            throw new UpdateException("Пользователя с id " + user.getId() + " нет");
        }
    }
}