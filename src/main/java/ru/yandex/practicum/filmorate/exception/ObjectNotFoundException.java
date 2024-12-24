package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
