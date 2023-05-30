package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
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

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        List<User> friends = new ArrayList<>();

        while (rowSet.next()) {
            User user = new User();
            user.setId(rowSet.getLong("USER_ID"));
            user.setName(rowSet.getString("NAME"));
            user.setEmail(rowSet.getString("EMAIL"));
            user.setLogin(rowSet.getString("LOGIN"));
            user.setBirthday(rowSet.getDate("BIRTHDAY").toLocalDate());
            friends.add(user);
        }

        return friends;
    }


    public List<User> getCommonFriends(long userId, long friendId) {
        String sqlQuery = "SELECT USERS.* FROM USERS " +
                "JOIN USER_FRIENDS UF1 ON USERS.USER_ID = UF1.FRIEND_ID " +
                "JOIN USER_FRIENDS UF2 ON USERS.USER_ID = UF2.FRIEND_ID " +
                "WHERE UF1.USER_ID = ? AND UF2.USER_ID = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, userId, friendId);
        List<User> commonFriends = new ArrayList<>();

        while (rowSet.next()) {
            User user = new User();
            user.setId(rowSet.getLong("USER_ID"));
            user.setName(rowSet.getString("NAME"));
            user.setEmail(rowSet.getString("EMAIL"));
            user.setLogin(rowSet.getString("LOGIN"));
            user.setBirthday(rowSet.getDate("BIRTHDAY").toLocalDate());
            commonFriends.add(user);
        }

        return commonFriends;
    }

}