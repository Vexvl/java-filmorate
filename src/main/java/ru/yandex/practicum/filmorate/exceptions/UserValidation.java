package ru.yandex.practicum.filmorate.exceptions;

import java.time.LocalDate;

public class UserValidation {
    public static void validateEmail(String email) throws ValidationException {
        if (email == null) {
            throw new ValidationException("Почта не должна быть пустой");
        } else if (!email.contains("@")) {
            throw new ValidationException("Почта должна содержать <@>");
        }
    }

    public static void validateLogin(String login) throws ValidationException {
        if (login == null) {
            throw new ValidationException("Логин не должнен быть пустым");
        } else if (login.contains(" ")) {
            throw new ValidationException("Логин содержит пробелы");
        }
    }

    public static void validateBirthday(LocalDate birthday) throws ValidationException {
        if (birthday.isBefore(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
