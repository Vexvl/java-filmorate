package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class FilmValidation {

    public static void validateName(String name) throws ValidationException {
        if (name == null) {
            log.warn("Имя не должно быть пустым");
            throw new ValidationException("Имя не должно быть пустым");
        }
    }

    public static void validateDescription(String description) throws ValidationException {
        if (description.length() > 200) {
            log.warn("Описание должно быть короче 200 символов");
            throw new ValidationException("Описание должно быть короче 200 символов");
        }
    }

    public static void validateReleaseDate(LocalDate date) throws ValidationException {
        LocalDate validationDate = LocalDate.of(1985, 12, 28);
        if (date.isBefore(validationDate)) {
            log.warn("Дата релиза раньше дня рождения кино");
            throw new ValidationException("Дата релиза раньше дня рождения кино");
        }
    }

    public static void validateDuration(Integer duration) throws ValidationException {
        if (duration <= 0) {
            log.warn("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
