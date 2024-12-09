package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

public class FilmManager {
    private final HashMap<Long, Film> films = new HashMap<>();
    private Long counter = 0L;

    public Collection<Film> getValues() {
        return films.values();
    }

    public Film create(Film film) {
        film.setId(++counter);
        films.put(counter, film);

        return film;
    }

    public Film get(Long id) {
        return films.get(id);
    }

    public Film update(Film film) {
        return films.put(film.getId(), film);
    }
}
