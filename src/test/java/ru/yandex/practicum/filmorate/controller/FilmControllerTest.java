package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {

    FilmController filmController;

    @BeforeEach
    void before() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
    }

    @Test
    void addAFilm() {
        Film film = new Film(1L, "Name 1", "Descr 1", new Mpa(1, "Test"), null, LocalDate.now(), 20);

        filmController.createFilm(film);

        assertEquals(1, filmController.getFilms().size(), "Возвращается неправильное количество");
    }
}
