package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserStorageDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class UserService {

    private final UserStorageDao userStorageDao;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private JdbcTemplate jdbcTemplate;

    public User createUser(User user) {
        return userStorageDao.create(user);
    }

    public User updateUser(User user) {
        return userStorageDao.update(user);
    }

    public User getUser(long id) {
        return userStorageDao.getUser(id);
    }

    public List<User> getUsers() {
        return userStorageDao.getUsers();
    }

    public void addFriend(long userId, long friendId) {
        userStorageDao.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        userStorageDao.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(long userId) {
        String sqlQuery = "SELECT USERS.USER_ID, USERS.NAME, USERS.EMAIL, USERS.LOGIN, USERS.BIRTHDAY " +
                "FROM USERS " +
                "JOIN USER_FRIENDS ON USERS.USER_ID = USER_FRIENDS.FRIEND_ID " +
                "WHERE USER_FRIENDS.USER_ID = ?";

        return jdbcTemplate.query(sqlQuery, new Object[]{userId}, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("USER_ID"));
            user.setName(rs.getString("NAME"));
            user.setEmail(rs.getString("EMAIL"));
            user.setLogin(rs.getString("LOGIN"));
            user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());
            return user;
        });
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        String sqlQuery = "SELECT USERS.* FROM USERS " +
                "JOIN USER_FRIENDS UF1 ON USERS.USER_ID = UF1.FRIEND_ID " +
                "JOIN USER_FRIENDS UF2 ON USERS.USER_ID = UF2.FRIEND_ID " +
                "WHERE UF1.USER_ID = :userId AND UF2.USER_ID = :friendId";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("friendId", friendId);

        return namedParameterJdbcTemplate.query(sqlQuery, parameters, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("USER_ID"));
            user.setName(rs.getString("NAME"));
            user.setEmail(rs.getString("EMAIL"));
            user.setLogin(rs.getString("LOGIN"));
            user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());
            return user;
        });
    }
}