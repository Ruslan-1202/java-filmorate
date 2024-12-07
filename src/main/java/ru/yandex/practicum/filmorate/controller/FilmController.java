package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

@RestController("/films")
public class FilmController {
    private final HashMap<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> get() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        return newFilm;
    }
}
