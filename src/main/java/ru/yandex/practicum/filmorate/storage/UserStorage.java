package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
    Optional<User> create(User user);

    Optional<User> get(Long id);

    Optional<User> update(User user);

    Collection<User> getValues();

    void putFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    Set<User> getFriends(User user);

    Set<User> getCommonFriends(User user, User otherUser);
}
