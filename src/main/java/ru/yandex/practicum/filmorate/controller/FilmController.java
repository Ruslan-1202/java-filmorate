package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;

@RestController()
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    final FilmService filmService;

    private static final int MIN_YEAR = 1895;
    private static final int MIN_MONTH = 12;
    private static final int MIN_DAY = 28;

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getValues();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.debug("Создание фильма");
        check(film);
        return filmService.create(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Обновление фильма");
        check(film);
        return filmService.update(film);
    }

    @GetMapping("{id}")
    public Film getFilm(@PathVariable("id") Long id) {
        log.debug("Получение одного фильма");
        return filmService.get(id);
    }

    @PutMapping("{id}/like/{userId}")
    public void setLike(@PathVariable("id") Long id,
                        @PathVariable("userId") Long userId) {
        log.debug("Ставим лайк фильму");
        filmService.setLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long id,
                           @PathVariable("userId") Long userId) {
        log.debug("Удаляем лайк фильму");
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> topLikes(@RequestParam(value = "count", defaultValue = "10") Long count) {
        log.debug("Запрос по количеству лайков");
        return filmService.topLikes(count);
    }

    private void check(Film film) throws ValidationException {
        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма не может быть отрицательной");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(MIN_YEAR, MIN_MONTH, MIN_DAY))) {
            log.error("Дата не может быть раньше {}.{}.{}", MIN_DAY, MIN_MONTH, MIN_YEAR);
            throw new ValidationException("Дата не может быть раньше 28.12.1895");
        }
    }
}
