package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();
    private final HashMap<User, Set<User>> friends = new HashMap<>();
    private Long counter = 0L;

    @Override
    public Optional<User> create(User user) {
        user.setId(++counter);
        users.put(counter, user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> get(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> update(User user) {
        return Optional.ofNullable(users.put(user.getId(), user));
    }

    @Override
    public Collection<User> getValues() {
        return users.values();
    }

    @Override
    public void putFriend(User user, User friend) {
        Set<User> userSet = friends.computeIfAbsent(user, a -> new HashSet<>());
        userSet.add(friend);

        Set<User> friendSet = friends.computeIfAbsent(friend, a -> new HashSet<>());
        friendSet.add(user);
    }

    @Override
    public void deleteFriend(User user, User friend) {
        Set<User> userSet = friends.computeIfAbsent(user, a -> new HashSet<>());
        userSet.remove(friend);

        Set<User> friendSet = friends.computeIfAbsent(friend, a -> new HashSet<>());
        friendSet.remove(user);
    }

    @Override
    public Set<User> getFriends(User user) {
        return friends.computeIfAbsent(user, a -> new HashSet<>());
    }

    @Override
    public Set<User> getCommonFriends(User user, User otherUser) {
        Set<User> userSet = friends.computeIfAbsent(user, a -> new HashSet<>());
        Set<User> otherUserSet = friends.computeIfAbsent(otherUser, a -> new HashSet<>());

        return userSet.stream()
                .filter(a -> otherUserSet.contains(a))
                .collect(Collectors.toSet());
    }
}
