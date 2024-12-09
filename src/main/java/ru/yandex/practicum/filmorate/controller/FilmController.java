package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmManager;

import java.time.LocalDate;
import java.util.Collection;

@RestController()
@RequestMapping("/films")
@Slf4j
public class FilmController {

    FilmManager filmManager = new FilmManager();

    private static final int MIN_YEAR = 1895;
    private static final int MIN_MONTH = 12;
    private static final int MIN_DAY = 28;

    @GetMapping
    public Collection<Film> getFilms() {
        return filmManager.getValues();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.debug("Создание фильма");

        check(film);
        return filmManager.create(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Обновление фильма");

        Film newFilm = film;
        Long oldId = newFilm.getId();
        Film oldFilm = filmManager.get(oldId);

        if (oldFilm == null) {
            log.error("Фильм c ID = {} не найден", oldId);
            throw new ValidationException("Фильм не найден");
        }

        check(newFilm);

        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setName(newFilm.getName());
        oldFilm.setDuration(newFilm.getDuration());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());

        return filmManager.update(oldFilm);
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
