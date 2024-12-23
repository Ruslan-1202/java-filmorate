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
import java.util.Set;

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
        try {
            check(user);
        } catch (EmptyNameException e) {
            user.setName(user.getLogin());
        }
        return userService.update(user);
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable("id") Long id) {
        log.debug("Получение одного пользователя");
        return userService.get(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void putFriend(@PathVariable("id") Long id,
                          @PathVariable("friendId") Long friendId) {
        log.debug("Добавление в друзья");
        userService.putFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Long id,
                             @PathVariable("friendId") Long friendId) {
        log.debug("Удаление из друзей");
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public Set<User> getFriends(@PathVariable("id") Long id) {
        log.debug("Список друзей");
        return userService.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable("id") Long id,
                                      @PathVariable("otherId") Long otherId) {
        log.debug("Список друзей, общих с другим пользователем");
        return userService.getCommonFriends(id, otherId);
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
