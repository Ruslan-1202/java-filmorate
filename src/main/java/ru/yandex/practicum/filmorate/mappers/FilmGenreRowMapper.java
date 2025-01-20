package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmGenreRowMapper implements RowMapper<FilmGenre> {
    @Override
    public FilmGenre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmGenre filmGenre = new FilmGenre();
        filmGenre.setFilmId(resultSet.getLong("film_id"));
        Genre genre = new Genre();
        genre.setId(resultSet.getLong("genre_id"));
        genre.setName(resultSet.getString("name"));
        filmGenre.setGenre(genre);
        return filmGenre;
    }
}
