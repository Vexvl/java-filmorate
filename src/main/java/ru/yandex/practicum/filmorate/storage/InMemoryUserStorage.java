package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> userMap = new HashMap<>();

    private long id = 1;

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(id);
        userMap.put(user.getId(), user);
        id++;
        log.info("Пользователь с id " + user.getId() + " успешно добавлен");
        return user;
    }

    @Override
    public User update(User user) {
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            log.info("Пользователь с id " + user.getId() + " успешно обновлён");
            return user;
        } else {
            throw new ExistingException("Пользователя с id " + user.getId() + " нет");
        }
    }

    @Override
    public User delete(User user) {
        if (userMap.containsValue(user)) {
            userMap.remove(user);
            return user;
        } else {
            throw new ExistingException("Такого пользователя нет");
        }
    }

    @Override
    public User getUser(long id) {
        if (userMap.containsKey(id)) {
            return userMap.get(id);
        } else {
            throw new ExistingException("Такого пользователя нет");
        }
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(userMap.values());
    }

}