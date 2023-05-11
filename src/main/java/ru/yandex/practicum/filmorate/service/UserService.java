package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.UserStorageDaoImp;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserStorageDao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class UserService {
    private final UserStorageDao userStorageDao;

    public UserService(UserStorageDaoImp userStorageDao) {
        this.userStorageDao = userStorageDao;
    }

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
        User user = userStorageDao.getUser(userId);
        User user2 = userStorageDao.getUser(friendId);
        user.getFriends().add(friendId);
        user2.getFriends().add(userId);
        userStorageDao.update(user);
        userStorageDao.update(user2);
    }

    public void deleteFriend(long userId, long friendId) {
        User user = userStorageDao.getUser(userId);
        User user2 = userStorageDao.getUser(friendId);
        user.getFriends().remove(user2.getId());
        user2.getFriends().remove(user.getId());
        userStorageDao.update(user);
        userStorageDao.update(user2);
    }

    public List<User> getFriends(long userId) {
        User user = userStorageDao.getUser(userId);
        List<User> friends = new ArrayList<>();
        for (Long id : user.getFriends()) {
            friends.add(userStorageDao.getUser(id));
        }
        return friends;
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        User user = userStorageDao.getUser(userId);
        User user2 = userStorageDao.getUser(friendId);
        Set<Long> userFriendSet = new HashSet<>(user.getFriends());
        Set<Long> user2FriendSet = new HashSet<>(user2.getFriends());
        userFriendSet.retainAll(user2FriendSet);
        List<User> commonFriends = new ArrayList<>();
        for (Long id : userFriendSet) {
            commonFriends.add(userStorageDao.getUser(id));
        }
        return commonFriends;
    }
}