package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

@Service
public class FilmService {
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
