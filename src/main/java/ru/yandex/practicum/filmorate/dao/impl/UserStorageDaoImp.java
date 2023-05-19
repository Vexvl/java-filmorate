package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        id++;
        user.setId(id);
        jdbcTemplate.update("INSERT INTO USERS (USER_ID, NAME, EMAIL, LOGIN, BIRTHDAY) VALUES (?,?,?,?,?)", user.getId(), user.getName(), user.getEmail(), user.getLogin(), user.getBirthday());
        return user;
    }

    @Override
    public User update(User user) {
        getUser(user.getId());
        jdbcTemplate.update("UPDATE USERS SET USER_ID=?, NAME=?, EMAIL=?, LOGIN=? ,BIRTHDAY=? WHERE USER_ID=?", user.getId(), user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
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
        return jdbcTemplate.query("SELECT * FROM USERS WHERE USER_ID=?", new Object[]{id}, new UserMapper()).stream().findAny().orElseThrow();
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM USERS", new UserMapper());
    }

    @Override
    public void addFriend(long userId, long userId2) {
        getUser(userId);
        getUser(userId2);
        String sql = "SELECT * FROM USER_FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId, userId2);
        if (sqlRowSet.next()) {
            sql = "UPDATE USER_FRIENDS SET CONFIRMATION_STATUS = TRUE WHERE USER_ID = ? AND FRIEND_ID = ?";
            jdbcTemplate.update(sql, userId, userId2);
            jdbcTemplate.update(sql, userId2, userId);
        } else {
            sql = "INSERT INTO USER_FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)";
            jdbcTemplate.update(sql, userId, userId2);
        }
    }

    @Override
    public void deleteFriend(long userId, long userId2) {
        getUser(userId);
        getUser(userId2);
        jdbcTemplate.update("DELETE FROM USER_FRIENDS WHERE USER_ID=? AND FRIEND_ID=?", userId, userId2);
        jdbcTemplate.update("UPDATE USER_FRIENDS SET CONFIRMATION_STATUS = FALSE WHERE USER_ID = ? AND FRIEND_ID =?", userId, userId2);
    }

    @Override
    public Set<Long> getFriendsId(long userId) {
        Set<Long> friends = new HashSet<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT FRIEND_ID FROM USER_FRIENDS WHERE USER_ID=?", userId);
        while (sqlRowSet.next()) {
            friends.add(sqlRowSet.getLong("FRIEND_ID"));
        }
        return friends;
    }

    @Override
    public List<User> getUsersByIds(List<Long> userIds) {
        List<User> users = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM USERS");
        while (sqlRowSet.next()) {
            users.add(getUser(sqlRowSet.getLong("USER_ID")));
        }
        return users;
    }
}