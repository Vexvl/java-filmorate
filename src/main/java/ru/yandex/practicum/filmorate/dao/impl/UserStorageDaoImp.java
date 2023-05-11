package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
@Slf4j
public class UserStorageDaoImp implements ru.yandex.practicum.filmorate.dao.UserStorageDao {
    private final JdbcTemplate jdbcTemplate;

    public UserStorageDaoImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User delete(User user) {
        return null;
    }

    @Override
    public User getUser(long id) {
        return null;
    }

    @Override
    public List<User> getUsers() {
        return null;
    }
}
