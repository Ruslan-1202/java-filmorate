package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EmptyNameException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController()
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Long, User> users = new HashMap<>();
    private Long counter = 0L;

    @GetMapping
    public Collection<User> getUsers() {
        log.debug("Получение всех записей");
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.debug("Создание пользователя");
        try {
            check(user);
        } catch (EmptyNameException e) {
            user.setName(user.getLogin());
        }

        user.setId(++counter);
        users.put(counter, user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("Обновление пользователя");
        User newUser = user;
        Long oldId = newUser.getId();
        User oldUser = users.get(oldId);

        if (oldUser == null) {
            log.error("Пользователь c ID = {} не найден", oldId);
            throw new ValidationException("Пользователь не найден");
        }

        try {
            check(newUser);
        } catch (EmptyNameException e) {
            newUser.setName(newUser.getLogin());
        }

        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setBirthday(newUser.getBirthday());

        users.put(oldId, oldUser);
        return oldUser;
    }

    private void check(User user) throws ValidationException, EmptyNameException {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            throw new EmptyNameException();
        }
    }
}
