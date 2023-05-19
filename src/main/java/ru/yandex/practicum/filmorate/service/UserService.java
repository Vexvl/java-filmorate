package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserStorageDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@AllArgsConstructor
public class UserService {

    private final UserStorageDao userStorageDao;

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
        List<User> friends = new ArrayList<>();
        for (Long id : userStorageDao.getFriendsId(userId)) {
            friends.add(userStorageDao.getUser(id));
        }
        return friends;
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        Set<Long> userFriendSet = new HashSet<>(userStorageDao.getFriendsId(userId));
        Set<Long> user2FriendSet = new HashSet<>(userStorageDao.getFriendsId(friendId));
        userFriendSet.retainAll(user2FriendSet);
        List<User> commonFriends = new ArrayList<>();
        for (Long id : userFriendSet) {
            commonFriends.add(userStorageDao.getUser(id));
        }
        return commonFriends;
    }
}