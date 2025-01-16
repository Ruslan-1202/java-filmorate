package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component("filmDbStorage")
@Primary
@RequiredArgsConstructor
@Repository
public class FilmDbStorage implements FilmStorage {

    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<Film> mapper;

    private final HashMap<Long, Film> films = new HashMap<>();
    private final HashMap<Film, Set<User>> likes = new HashMap<>();
    private Long counter = 0L;

    @Override
    public Collection<Film> getValues() {
        List<Film> films = jdbc.query("SELECT * FROM \"films\" f LEFT JOIN \"mpa\" m ON f.\"mpa_id\" = m.\"id\"", mapper);
        //   List<Film> films = jdbc.query("SELECT * FROM \"films\"", mapper);

        return films;
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
//        Batch
        return Optional.ofNullable(films.put(film.getId(), film));
    }

    @Override
    public void setLike(Film film, User user) {
        Set<User> users = likes.computeIfAbsent(film, a -> new HashSet<>());
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
