package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final List<User> userList = new ArrayList<>();
    private int id = 1;

    private void addUserToList(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(id);
        userList.add(user);
        log.info("Пользователь успешно создан");
        id++;
    }

    private User updateUserList(User user) {
        if (userList.contains(user)) {
            userList.remove(user);
            userList.add(user);
            log.info("Пользователь успешно обновлён");
        } else {
            userList.add(user);
            log.info("Такого пользователя нет, он был создан");
        }
        return user;
    }

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
        return userList;
    }
}