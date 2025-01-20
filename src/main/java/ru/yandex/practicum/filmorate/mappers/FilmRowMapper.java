package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("films.id"));
        film.setName(resultSet.getString("films.name"));
        film.setDescription(resultSet.getString("description"));
        film.setDuration(resultSet.getInt("duration"));

        try {
            Mpa mpa = new Mpa(resultSet.getLong("mpa.id"), resultSet.getString("mpa.name"));
            film.setMpa(mpa);
        } catch (Exception e) {
            film.setMpa(null);
        }

        java.sql.Date sqlDate = resultSet.getDate("release_date");
        if (sqlDate != null) {
            LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
            film.setReleaseDate(releaseDate);
        }

        return film;
    }
}
