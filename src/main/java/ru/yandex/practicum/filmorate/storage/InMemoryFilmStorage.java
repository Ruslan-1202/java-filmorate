package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();
    private final HashMap<Film, Set<User>> likes = new HashMap<>();
    private Long counter = 0L;

    @Override
    public Collection<Film> getValues() {
        return films.values();
    }

    @Override
    public Optional<Film> create(Film film) {
        film.setId(++counter);
        films.put(counter, film);
        return Optional.ofNullable(film);
    }

    @Override
    public Optional<Film> get(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Optional<Film> update(Film film) {
        return Optional.ofNullable(films.put(film.getId(), film));
    }

    @Override
    public void setLike(Film film, User user) {
        Set<User> users = likes.computeIfAbsent(film, a -> new HashSet<User>());
        users.add(user);
    }

    @Override
    public Set<User> getLikeUsers(Film film) {
        return likes.getOrDefault(film, new HashSet<>());
    }

    @Override
    public void deleteLike(Film film, User user) {
        Set<User> users = likes.get(film);
        users.remove(user);
    }

    @Override
    public long getCountLikes(Film film) {
        return likes.getOrDefault(film, new HashSet<>()).size();
    }
}
