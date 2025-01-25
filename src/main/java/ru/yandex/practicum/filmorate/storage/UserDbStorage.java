package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component("userDbStorage")
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private static final String SELECT_USERS = "SELECT * FROM users ";
    private static final RowMapper<User> userMapper = new UserRowMapper();

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<User> create(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", user.getName());
        params.addValue("login", user.getLogin());
        params.addValue("email", user.getEmail());
        params.addValue("birthday", user.getBirthday());

        jdbc.update("INSERT INTO users (name, login, email, birthday) " +
                "VALUES (:name, :login, :email, :birthday)", params, keyHolder, new String[]{"id"});
        user.setId(keyHolder.getKey().longValue());
        return Optional.of(user);
    }

    @Override
    public Optional<User> get(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        User user;
        try {
            user = jdbc.queryForObject(SELECT_USERS + " WHERE id = :id", params, userMapper);
        } catch (EmptyResultDataAccessException e) {
            user = null;
        }

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> update(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", user.getId());
        params.addValue("name", user.getName());
        params.addValue("login", user.getLogin());
        params.addValue("email", user.getEmail());
        params.addValue("birthday", user.getBirthday());

        jdbc.update("UPDATE users SET name = :name, " +
                "login = :login, " +
                "email = :email, " +
                "birthday = :birthday " +
                "WHERE id = :id", params);

        return Optional.ofNullable(user);
    }

    @Override
    public Collection<User> getValues() {
        return jdbc.query(SELECT_USERS, userMapper);
    }

    @Override
    public void putFriend(User user, User friend) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", user.getId());
        params.addValue("friend_id", friend.getId());

        jdbc.update("INSERT INTO friends (user_id, friend_id) VALUES (:user_id, :friend_id)", params);
    }

    @Override
    public void deleteFriend(User user, User friend) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", user.getId());
        params.addValue("friend_id", friend.getId());

        jdbc.update("DELETE FROM friends " +
                "WHERE user_id = :user_id AND friend_id = :friend_id", params);
    }

    @Override
    public Set<User> getFriends(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", user.getId());

        return jdbc.query("SELECT u.* FROM users u " +
                        "JOIN friends f ON f.friend_id = u.id " +
                        "WHERE f.user_id = :user_id", params, userMapper)
                .stream()
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getCommonFriends(User user, User otherUser) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", user.getId());
        params.addValue("other_user_id", otherUser.getId());

        return jdbc.query("SELECT u.* FROM users u " +
                        "JOIN friends f ON f.friend_id = u.id " +
                        "JOIN friends f1 ON f1.friend_id = u.id " +
                        "WHERE f.user_id = :user_id " +
                        "AND f1.user_id = :other_user_id ", params, userMapper)
                .stream()
                .collect(Collectors.toSet());
    }
}
