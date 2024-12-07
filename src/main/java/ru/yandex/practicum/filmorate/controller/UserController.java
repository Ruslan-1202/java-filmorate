package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@RestController("/users")
public class UserController {
    private final HashMap<Long, User> users = new HashMap<>();
    Long id = 0L;

    @GetMapping
    public Collection<User> get() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        Long oldId = newUser.getId();
        User oldUser;

        oldUser = users.get(id);
        return oldUser;
    }
}
