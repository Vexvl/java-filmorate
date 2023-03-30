package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserValidation;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    private Map<Integer, User> userList = new HashMap<>();
    private int id = 1;

    private void validate(User user) throws ValidationException {
        UserValidation.validateEmail(user.getEmail());
        UserValidation.validateBirthday(user.getBirthday());
        UserValidation.validateLogin(user.getLogin());
    }

    private void addUserToList(User user) throws ValidationException {
        if(user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        validate(user);
        user.setId(id);
        userList.put(id, user);
        log.info("Пользователь успешно создан");
        id++;
    }

    private void updateFilmList(User user) throws ValidationException {
        if (userList.containsKey(user.getId())) {
            validate(user);
            userList.put(user.getId(), user);
            log.info("Фильм успешно обновлён");
        } else log.warn("Нужно сначала создать фильм");
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) throws ValidationException {
        addUserToList(user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        updateFilmList(user);
        return user;
    }

    @GetMapping("/users")
    public Map<Integer, User> getUsers() {
        return userList;
    }
}