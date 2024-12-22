package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.StorageException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService {

    final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getValues() {
        return filmStorage.getValues();
    }

    public Film create(Film film) {
        return filmStorage.create(film).orElseThrow(() -> new StorageException("Не удалось создать фильм"));
    }

    public Film get(Long id) {
        return filmStorage.get(id).orElseThrow(() -> new ObjectNotFoundException("Фильм c id = " + id + " не найден"));
    }

    public Film update(Film film) {
        Long id = film.getId();
        Film oldFilm = get(id);

        oldFilm.setDescription(film.getDescription());
        oldFilm.setName(film.getName());
        oldFilm.setDuration(film.getDuration());
        oldFilm.setReleaseDate(film.getReleaseDate());

        return filmStorage.update(film).orElseThrow(() -> new StorageException("Не удалось обновить фильм id = " + id));
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
        Map<Film, Long> topLikes =
                filmStorage.getValues().stream()
                        //.map(b->filmStorage.getCountLikes(b)))
                        .collect(Collectors.toMap(a -> a, a -> filmStorage.getCountLikes(a)));

        //  topLikes.
        return topLikes.keySet();
    }

    private User getUser(Long id) {
        //TODO сделать получение юзера
        return Optional.of(new User(id, "em@em.ru", "qq", "name 1", LocalDate.now()))
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь c id = " + id + " не найден"));
    }

}
