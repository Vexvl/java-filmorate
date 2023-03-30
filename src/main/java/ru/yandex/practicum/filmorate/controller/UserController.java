package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserValidation;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    private final List<User> userList = new ArrayList<>();
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
        userList.add(user);
        log.info("Пользователь успешно создан");
        id++;
    }

    private void updateFilmList(User user) throws ValidationException {
        if (userList.contains(user)) {
            validate(user);
            userList.add(user);
            log.info("Пользователь успешно обновлён");
        } else log.warn("Нужно сначала создать пользователя");
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
    public List<User> getUsers() {
        return userList;
    }
}