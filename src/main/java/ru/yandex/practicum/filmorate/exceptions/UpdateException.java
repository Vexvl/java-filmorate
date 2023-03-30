package ru.yandex.practicum.filmorate.exceptions;

public class UpdateException extends RuntimeException {

    public UpdateException(String message) {
        super(message);
    }


    public String getDetailMessage() {
        return "Ошибка обновления: " + getMessage();
    }
}
