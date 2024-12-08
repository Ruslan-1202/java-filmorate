package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {

    UserController userController;

    @BeforeEach
    void before() {
        userController = new UserController();
    }
    @Test
    void Add1Film() {
        User user = new User(1L, "Email 1", "Login 1", "Name 1", LocalDate.now());

        userController.createUser(user);

        assertEquals(1, userController.getUsers().size(), "Возвращается неправильное количество");
    }
}
