package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.StorageException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@AllArgsConstructor
public class FilmService {

    @Qualifier("filmDbStorage") // не работает, почему? Работает только @Primary
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getValues() {
        return filmStorage.getValues();
    }

    public Film create(Film film) {
        return filmStorage.create(film)
                .orElseThrow(() -> new StorageException("Не удалось создать фильм"));
    }

    public Film get(Long id) {
        return filmStorage.get(id)
                .orElseThrow(() -> new ObjectNotFoundException("Фильм c id = " + id + " не найден"));
    }

    public Film update(Film film) {
        Long id = film.getId();
        Film oldFilm = get(film.getId());

        oldFilm.setDescription(film.getDescription());
        oldFilm.setName(film.getName());
        oldFilm.setDuration(film.getDuration());
        oldFilm.setReleaseDate(film.getReleaseDate());

        return filmStorage.update(oldFilm)
                .orElseThrow(() -> new StorageException("Не удалось обновить фильм id = " + id));
    }

    public void setLike(Long filmId, Long userId) {
        Film film = get(filmId);
        User user = getUser(userId);
        if (filmStorage.getLikeUsers(film).contains(user)) {
            throw new StorageException("Пользователь " + userId + " уже ставил лайк фильму " + filmId);
        }
        filmStorage.setLike(film, user);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = get(filmId);
        User user = getUser(userId);
        if (!filmStorage.getLikeUsers(film).contains(user)) {
            throw new StorageException("Пользователь " + userId + " не ставил лайк фильму " + filmId);
        }
        filmStorage.deleteLike(film, user);
    }

    public Collection<Film> topLikes(Long count) {
        return filmStorage.topLikes(count);
    }

    private User getUser(Long id) {
        return userStorage.get(id)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь c id = " + id + " не найден"));
    }

}
