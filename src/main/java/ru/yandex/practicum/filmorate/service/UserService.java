package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@Service
public class UserService {
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
        return users.put(user.getId(), user);
    }
}
