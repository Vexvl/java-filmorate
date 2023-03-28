package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmValidation;
import ru.yandex.practicum.filmorate.exceptions.UserValidation;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private void validate(User user) throws ValidationException {
        UserValidation.validateEmail(user.getEmail());
        UserValidation.validateBirthday(user.getBirthday());
        UserValidation.validateLogin(user.getLogin());
    }

    private List<User> userList = new ArrayList<>();

    @PostMapping("/user")
    public User createUser(@RequestBody User user) throws ValidationException {
        userList.add(user);
        validate(user);
        return user;
    }

    @PutMapping("/user")
    public List<User> updateUser(@RequestBody User user) throws ValidationException {
        if (userList.contains(user)) {
            userList.remove(user);
            userList.add(user);
            validate(user);
        }
        return userList;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userList;
    }
}