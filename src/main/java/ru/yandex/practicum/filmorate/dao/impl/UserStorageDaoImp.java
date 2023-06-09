package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
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
        return user;
    }

    @Override
    public User getUser(long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID=?", id);
        if (sqlRowSet.next()) {
            User user = new User();

            user.setId(sqlRowSet.getLong("USER_ID"));
            user.setName(sqlRowSet.getString("NAME"));
            user.setEmail(sqlRowSet.getString("EMAIL"));
            user.setLogin(sqlRowSet.getString("LOGIN"));
            user.setBirthday(sqlRowSet.getDate("BIRTHDAY").toLocalDate());
            return user;
        } else {
            throw new ExistingException("Такого жанра нет");
        }
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM USERS");
        while (rowSet.next()) {
            User user = new User();
            user.setId(rowSet.getLong("USER_ID"));
            user.setName(rowSet.getString("NAME"));
            user.setEmail(rowSet.getString("EMAIL"));
            user.setLogin(rowSet.getString("LOGIN"));
            user.setBirthday(rowSet.getDate("BIRTHDAY").toLocalDate());
            users.add(user);
        }

        return users;
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
        String sqlQuery = "SELECT FRIEND_ID FROM USER_FRIENDS WHERE USER_ID=?";
        List<Long> friendIds = jdbcTemplate.queryForList(sqlQuery, Long.class, userId);
        return new HashSet<>(friendIds);
    }

}