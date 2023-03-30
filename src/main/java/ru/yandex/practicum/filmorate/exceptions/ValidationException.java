package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }

    public static void validateReleaseDate(LocalDate date) throws ValidationException {
        LocalDate validationDate = LocalDate.of(1895, 12, 28);
        if (date.isBefore(validationDate)) {
            log.warn("Дата релиза раньше дня рождения кино");
            throw new ValidationException("Дата релиза раньше дня рождения кино");
        }
    }
}