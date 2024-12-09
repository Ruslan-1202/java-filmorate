package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

public class UserManager {
    private final HashMap<Long, User> users = new HashMap<>();
    private Long counter = 0L;

    public Collection<User> getValues() {
        return users.values();
    }

    public User create(User user) {
        user.setId(++counter);
        users.put(counter, user);

        return user;
    }

    public User get(Long id) {
        return users.get(id);
    }

    public User update(User user) {
        users.put(user.getId(), user);
    }
}
