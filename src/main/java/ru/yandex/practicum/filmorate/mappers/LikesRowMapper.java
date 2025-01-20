package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Likes;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikesRowMapper implements RowMapper<Likes> {
    @Override
    public Likes mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Likes likes = new Likes();
        likes.setFilmId(resultSet.getLong("film_id"));

        UserRowMapper userRowMapper = new UserRowMapper();
        likes.setUser(userRowMapper.mapRow(resultSet, rowNum));

        return likes;
    }
}
