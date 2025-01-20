package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Component()
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<Mpa> mapper;

    @Override
    public Optional<Mpa> getById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        Mpa mpa = null;
        try {
            mpa = jdbc.queryForObject("SELECT * FROM \"mpa\" WHERE \"id\" = :id", params, mapper);
        } catch (EmptyResultDataAccessException e) {

        }

        return Optional.ofNullable(mpa);
    }

    @Override
    public Collection<Mpa> getAll() {
        return jdbc.query("SELECT * FROM \"mpa\" ", mapper);
    }
}
