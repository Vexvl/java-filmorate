package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
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

    private final Map<Integer, User> userMap = new HashMap<>();

    private int id = 1;

    private void addUserToList(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(id);
        userMap.put(user.getId(), user);
        log.info("Пользователь успешно создан");
        id++;
    }

    private User updateUserList(User user) {
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            log.info("Пользователь успешно обновлён");
            return user;
        } else throw new UpdateException("Такого пользователя нет");
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
        return new ArrayList<User>(userMap.values());
    }
}