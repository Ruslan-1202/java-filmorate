package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {

    Collection<Film> getValues();

    Optional<Film> create(Film film);

    Optional<Film> get(Long id);

    Optional<Film> update(Film film);

    Set<User> getLikeUsers(Film film);

    void setLike(Film film, User user);

    void deleteLike(Film film, User user);

    long getCountLikes(Film film);
}
