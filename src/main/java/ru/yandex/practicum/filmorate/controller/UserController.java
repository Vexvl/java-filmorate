package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

/*@RestController
public class UserController {

    private List<User> userList = new ArrayList<>();

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        User user = new User();
    }

    @GetMapping("/film")
    public String homePage() {
        return "Котограм";
    }

    @GetMapping("/film")
    public String homePage() {
        return "Котограм";
    }

}

/*
    создание пользователя;
    обновление пользователя;
    получение списка всех пользователей.
 */