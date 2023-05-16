package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmMapper;
import ru.yandex.practicum.filmorate.service.MpaRatingMapper;
import ru.yandex.practicum.filmorate.service.UserMapper;

import java.util.List;

@Component
@Slf4j
public class UserStorageDaoImp implements ru.yandex.practicum.filmorate.dao.UserStorageDao {
    private final JdbcTemplate jdbcTemplate;

    private long id = 0;

    public UserStorageDaoImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        id++;
        user.setId(id);
        jdbcTemplate.update("INSERT INTO USERS (USER_ID, NAME, EMAIL, LOGIN, BIRTHDAY) VALUES (?,?,?,?,?)", user.getId(), user.getName(), user.getEmail(), user.getLogin(), user.getBirthday());
        return user;
    }

    @Override
    public User update(User user) {
        if (getUser(user.getId()) != null) {
            jdbcTemplate.update("UPDATE USERS SET USER_ID=?, NAME=?, EMAIL=?, LOGIN=? ,BIRTHDAY=? WHERE USER_ID=?", user.getId(), user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(),user.getId());
        }
        else throw new ExistingException("Такого film нет");
        return user;
    }

    @Override
    public User delete(User user) {
        jdbcTemplate.update("DELETE FROM USERS WHERE USER_ID=?", user.getId());
        log.info("Удален user: {} {}", user.getId(), user.getName());
        return user;
    }

    @Override
    public User getUser(long id) {
        return jdbcTemplate.query("SELECT * FROM USERS WHERE USER_ID=?", new Object[]{id}, new UserMapper()).stream().findAny().orElse(null);
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM USERS", new UserMapper());
    }

    @Override
    public void addFriend(long userId, long userId2) {
        jdbcTemplate.update("INSERT INTO USER_FRIENDS (USER_ID, FRIEND_ID, CONFIRMATION_STATUS) VALUES(?,?,?)",userId, userId2, false);
    }

    @Override
    public void deleteFriend(long userId, long userId2) {
        jdbcTemplate.update("DELETE FROM USER_FRIENDS WHERE USER_ID=? AND FRIEND_ID=?",userId, userId2);
        jdbcTemplate.update("UPDATE USER_FRIENDS SET CONFIRMATION_STATUS = FALSE WHERE USER_ID = ? AND FRIEND_ID =?", userId, userId2);
    }
}
