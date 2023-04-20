package ru.yandex.practicum.filmorate.exceptions;

public class ServerException extends RuntimeException {
    public ServerException(String message) {
        super(message);
    }
}
