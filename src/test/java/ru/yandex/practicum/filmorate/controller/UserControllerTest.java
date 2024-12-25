package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {

    UserController userController;

    @BeforeEach
    void before() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    void addAUser() {
        User user = new User(1L, "email@em.tu", "Login 1",
                "Name 1", LocalDate.of(1981, 1, 1));

        userController.createUser(user);

        assertEquals(1, userController.getUsers().size(), "Возвращается неправильное количество");
    }
}
