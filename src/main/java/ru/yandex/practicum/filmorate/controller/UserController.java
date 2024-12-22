package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EmptyNameException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;

@RestController()
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    final UserService userService;

    @GetMapping
    public Collection<User> getUsers() {
        log.debug("Получение всех записей");
        return userService.getValues();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.debug("Создание пользователя");
        try {
            check(user);
        } catch (EmptyNameException e) {
            user.setName(user.getLogin());
        }

        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("Обновление пользователя");
        User newUser = user;
        Long oldId = newUser.getId();
        User oldUser = userService.get(oldId);

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

        return userService.update(oldUser);
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable("id") Long id) {
        log.debug("Получение одного фильма");
        return userService.get(id);
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
