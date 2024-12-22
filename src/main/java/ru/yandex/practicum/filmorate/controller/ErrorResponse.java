package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    String message;
}

//public class ErrorResponse {
//    // название ошибки
//    String message;
//
//    public ErrorResponse(String message) {
//        this.message = message;
//    }
//
//    // геттеры необходимы, чтобы Spring Boot мог получить значения полей
//    public String getMessage() {
//        return message;
//    }
//}
