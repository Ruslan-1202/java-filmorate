package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController()
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<Long, Film> films = new HashMap<>();
    private Long counter = 0L;

    private static final int MIN_YEAR = 1895;
    private static final int MIN_MONTH = 12;
    private static final int MIN_DAY = 28;

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.debug("Создание фильма");

        check(film);
        film.setId(++counter);
        films.put(counter, film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Обновление фильма");

        Film newFilm = film;
        Long oldId = newFilm.getId();
        Film oldFilm = films.get(oldId);

        if (oldFilm == null) {
            log.error("Фильм c ID = {} не найден", oldId);
            throw new ValidationException("Фильм не найден");
        }

        check(newFilm);

        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setName(newFilm.getName());
        oldFilm.setDuration(newFilm.getDuration());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());

        films.put(oldId, oldFilm);
        return oldFilm;
    }

    private void check(Film film) throws ValidationException {
        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма не может быть отрицательной");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(MIN_YEAR, MIN_MONTH, MIN_DAY))) {
            log.error("Дата не может быть раньше 28.12.1895");
            throw new ValidationException("Дата не может быть раньше 28.12.1895");
        }
    }
}
