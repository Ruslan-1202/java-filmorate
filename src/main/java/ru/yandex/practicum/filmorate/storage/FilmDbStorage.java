package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;

import java.util.*;
import java.util.stream.Collectors;

@Component("filmDbStorage")
@Primary
@RequiredArgsConstructor
@Repository
public class FilmDbStorage implements FilmStorage {
    private static final String SELECT_FILMS = "SELECT * FROM films f " +
            "LEFT JOIN mpa m ON f.mpa_id = m.id";

    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<Film> mapper;
    private final RowMapper<Genre> mapperGenre;
    private final RowMapper<FilmGenre> mapperFilmGenre;
    private final RowMapper<Likes> likesRowMapperikesRowMapper;

    @Override
    public Collection<Film> getValues() {
        //берем фильмы + рейтинг
        List<Film> films = jdbc.query(SELECT_FILMS, mapper);
        //берем ид фильмов + жанры
        List<FilmGenre> filmGenres = jdbc.getJdbcOperations().query("SELECT * FROM film_genres fg " +
                "JOIN genres g ON fg.genre_id = g.id", mapperFilmGenre);
        //для фильмов получаем списки жанров
        for (Film film : films) {
            film.setGenres(filmGenres.stream()
                    .filter(a -> a.getFilmId() == film.getId())
                    .map(a -> a.getGenre())
                    .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        }
        return films;
    }

    @Override
    public Optional<Film> get(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        List<Film> films = jdbc.query(SELECT_FILMS + " WHERE f.id = :id", params, mapper);
        if (films.isEmpty()) {
            throw new ObjectNotFoundException("Фильм с id = " + id + " не найден");
        }

        Film film = films.get(0);

        List<Genre> genres = jdbc.query("SELECT * FROM film_genres fg" +
                " JOIN genres g ON fg.genre_id = g.id" +
                " WHERE film_id = :id", params, mapperGenre);
        film.setGenres(genres.stream().collect(Collectors.toCollection(LinkedHashSet::new)));

        return Optional.ofNullable(films.get(0));
    }

    @Override
    public Optional<Film> create(Film film) {
        checkUpdate(film);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("mpa_id", film.getMpa().getId());
        params.addValue("releaseDate", film.getReleaseDate());
        params.addValue("duration", film.getDuration());

        jdbc.update("INSERT INTO films (name, description, mpa_id, release_date, duration) " +
                "VALUES (:name, :description, :mpa_id, :releaseDate, :duration)", params, keyHolder, new String[]{"id"});
        film.setId(keyHolder.getKey().longValue());

        updateGenres(film);

        return Optional.ofNullable(film);
    }

    private void checkUpdate(Film film) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genres", film.getGenres());

        int count;
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            params.addValue("genres", film.getGenres().stream().map(a -> a.getId()).collect(Collectors.toList()));
            count = jdbc.queryForObject("SELECT COUNT(*) FROM genres " +
                    "WHERE id IN (:genres)", params, Integer.class);

            if (count != film.getGenres().size()) {
                throw new ValidationException("Не корретно заданы жанры");
            }
        }
        params = new MapSqlParameterSource();
        params.addValue("mpa_id", film.getMpa().getId());

        count = jdbc.queryForObject("SELECT COUNT(*) FROM mpa " +
                "WHERE id = :mpa_id", params, Integer.class);

        if (count != 1) {
            throw new ValidationException("Не корретно задан рейтинг");
        }
    }

    private void updateGenres(Film film) {
        Long filmId = film.getId();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);

        jdbc.update("DELETE FROM film_genres " +
                "WHERE film_id = :film_id", params);

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }

        SqlParameterSource[] batch = film.getGenres().stream()
                .map(a -> new MapSqlParameterSource()
                        .addValue("film_id", filmId)
                        .addValue("genre_id", a.getId())
                )
                .toArray(SqlParameterSource[]::new);

        jdbc.batchUpdate("INSERT INTO film_genres (film_id, genre_id) VALUES(:film_id, :genre_id)", batch);
    }

    @Override
    public Optional<Film> update(Film film) {
        checkUpdate(film);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", film.getId());
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("mpa_id", film.getMpa().getId());
        params.addValue("releaseDate", film.getReleaseDate());
        params.addValue("duration", film.getDuration());

        jdbc.update("UPDATE films SET name = :name, " +
                "description = :description, " +
                "mpa_id = :mpa_id, " +
                "release_date = :releaseDate, " +
                "duration = :duration " +
                "WHERE id = :id", params);

        updateGenres(film);
        return Optional.ofNullable(film);
    }

    @Override
    public void setLike(Film film, User user) {
//        if (isLikeExists(film, user)) {
//            throw new ValidationException("Этот пользователь уже ставил лайк фильму");
//        };

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", film.getId());
        params.addValue("user_id", user.getId());

        jdbc.update("MERGE INTO likes (film_id, user_id) VALUES (:film_id, :user_id)", params);
    }

    @Override
    public Set<User> getLikeUsers(Film film) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", film.getId());

        Set<User> users = jdbc.query("SELECT l.film_id, u.* FROM likes l " +
                        "JOIN users u ON l.user_id = u.id " +
                        "WHERE film_id = :film_id", params, likesRowMapperikesRowMapper).stream()
                .map(a -> a.getUser())
                .collect(Collectors.toCollection(HashSet::new));
        return users;
    }

    @Override
    public void deleteLike(Film film, User user) {
        if (!isLikeExists(film, user)) {
            throw new ValidationException("Этот пользователь не ставил лайк фильму");
        }
        ;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", film.getId());
        params.addValue("user_id", user.getId());

        jdbc.update("DELETE FROM likes " +
                "WHERE film_id = :film_id AND user_id = :user_id", params);
    }

    private boolean isLikeExists(Film film, User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", film.getId());
        params.addValue("user_id", user.getId());

        return jdbc.queryForObject("""
                SELECT COUNT(*) FROM likes WHERE film_id = :film_id AND user_id = :user_id
                """, params, Long.class) == 1L;
    }

    @Override
    public long getCountLikes(Film film) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", film.getId());

        return jdbc.queryForObject("SELECT COUNT(*) FROM likes " +
                "WHERE film_id = :film_id", params, Long.class);
    }

    @Override
    public Collection<Film> topLikes(Long count) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("count", count);

        List<Film> films = jdbc.query("""
                        SELECT f.*,
                               mpa.*,
                               (SELECT COUNT(*) FROM likes WHERE likes.film_id = f.id) as like_count
                        FROM films f
                        JOIN mpa ON f.mpa_id = mpa.id
                        ORDER BY like_count DESC
                        LIMIT :count
                """, params, mapper);

        if (films == null) {
            return films;
        }

        params = new MapSqlParameterSource();
        params.addValue("films", films.stream().map(Film::getId).collect(Collectors.toList()));

        //жанры для выбранных фильмов
        List<FilmGenre> filmGenres = jdbc.query("""
                SELECT * FROM film_genres fg
                JOIN genres g ON fg.genre_id = g.id
                WHERE fg.film_id IN (:films) """, params, mapperFilmGenre);
        //для фильмов получаем списки жанров
        for (Film film : films) {
            film.setGenres(filmGenres.stream()
                    .filter(a -> a.getFilmId() == film.getId())
                    .map(a -> a.getGenre())
                    .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        }

        return films;
    }
}
