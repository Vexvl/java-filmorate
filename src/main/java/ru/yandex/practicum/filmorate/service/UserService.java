package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class UserService {

    private UserStorage userStorage;
    private User user;
    private User user2;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(long userId, long friendId) {
        user = userStorage.getUser(userId);
        user2 = userStorage.getUser(friendId);
        user.getFriends().add(friendId);
        user2.getFriends().add(userId);
        userStorage.update(user);
        userStorage.update(user2);
    }

    public void deleteFriend(long userId, long friendId) {
        user = userStorage.getUser(userId);
        user2 = userStorage.getUser(friendId);
        user.getFriends().remove(user2.getId());
        user2.getFriends().remove(user.getId());
        userStorage.update(user);
        userStorage.update(user2);
    }

    public List<User> getFriends(long userId) {
        user = userStorage.getUser(userId);
        List<User> friends = new ArrayList<>();
        for (Long id : user.getFriends()){
            friends.add(userStorage.getUser(id));
        }
        return friends;
    }

    public List<User> getCommonFriends(long userId, long friendId){
        user = userStorage.getUser(userId);
        user2 = userStorage.getUser(friendId);
        Set<Long> userFriendSet = new HashSet<>(user.getFriends());
        Set<Long> user2FriendSet = new HashSet<>(user2.getFriends());
        userFriendSet.retainAll(user2FriendSet);
        List<User> commonFriends = new ArrayList<>();
        for (Long id : userFriendSet){
            commonFriends.add(userStorage.getUser(id));
        }
        return commonFriends;
    }
}