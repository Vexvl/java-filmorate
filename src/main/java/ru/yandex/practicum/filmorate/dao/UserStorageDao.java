package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorageDao {
    User create(User user);

    User update(User user);

    User delete(User user);

    User getUser(long id);

    List<User> getUsers();
}
