package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class UserValidation {
    public static void validateEmail(String email) throws ValidationException {
        if (email == null) {
            log.warn("Почта не должна быть пустой");
            throw new ValidationException("Почта не должна быть пустой");
        } else if (!email.contains("@")) {
            log.warn("Почта должна содержать <@>");
            throw new ValidationException("Почта должна содержать <@>");
        }
    }

    public static void validateLogin(String login) throws ValidationException {
        if (login == null) {
            log.warn("Логин не должнен быть пустым");
            throw new ValidationException("Логин не должнен быть пустым");
        } else if (login.contains(" ")) {
            log.warn("Логин содержит пробелы");
            throw new ValidationException("Логин содержит пробелы");
        }
    }

    public static void validateBirthday(LocalDate birthday) throws ValidationException {
        if (birthday.isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}