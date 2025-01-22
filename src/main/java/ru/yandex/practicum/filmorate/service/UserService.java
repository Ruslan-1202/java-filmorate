package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.StorageException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public Collection<User> getValues() {
        return userStorage.getValues();
    }

    public User create(User user) {
        return userStorage.create(user).orElseThrow(() -> new StorageException("Не удалось создать пользователя"));
    }

    public User get(Long id) {
        return userStorage.get(id)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь c id = " + id + " не найден"));
    }

    public User update(User user) {
        Long id = user.getId();
        User oldUser = get(id);

        oldUser.setLogin(user.getLogin());
        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setBirthday(user.getBirthday());
        return userStorage.update(oldUser)
                .orElseThrow(() -> new StorageException("Не удалось обновить пользователя с id = " + id));
    }

    public void putFriend(Long id, Long friendId) {
        User user = get(id);
        User friend = get(friendId);

        userStorage.putFriend(user, friend);
    }

    public void deleteFriend(Long id, Long friendId) {
        User user = get(id);
        User friend = get(friendId);

        userStorage.deleteFriend(user, friend);
    }

    public Set<User> getFriends(Long id) {
        User user = get(id);
        return userStorage.getFriends(user);
    }

    public Set<User> getCommonFriends(Long id, Long otherId) {
        User user = get(id);
        User otherUser = get(otherId);
        return userStorage.getCommonFriends(user, otherUser);
    }
}
