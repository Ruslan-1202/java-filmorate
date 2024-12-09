package ru.yandex.practicum.filmorate.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmptyNameException extends RuntimeException {
    public EmptyNameException(String message) {
        super(message);
    }
}
