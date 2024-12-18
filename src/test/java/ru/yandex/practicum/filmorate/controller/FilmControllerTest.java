package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {

    FilmController filmController;

    @BeforeEach
    void before() {
        filmController = new FilmController(new FilmService());
    }

    @Test
    void addAFilm() {
        Film film = new Film(1L, "Name 1", "Descr 1", LocalDate.now(), 20);

        filmController.createFilm(film);

        assertEquals(1, filmController.getFilms().size(), "Возвращается неправильное количество");
    }
}
