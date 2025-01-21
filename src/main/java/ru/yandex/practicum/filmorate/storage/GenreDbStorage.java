package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Component()
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<Genre> mapper;

    @Override
    public Optional<Genre> getById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        Genre genre;
        try {
            genre = jdbc.queryForObject("SELECT * FROM genres WHERE id = :id", params, mapper);
        } catch (EmptyResultDataAccessException e) {
            genre = null;
        }

        return Optional.ofNullable(genre);
    }

    @Override
    public Collection<Genre> getAll() {
        return jdbc.query("SELECT * FROM genres ", mapper);
    }
}
