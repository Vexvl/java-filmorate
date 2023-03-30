package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmValidation;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class FilmController {

    private final List<Film> filmList = new ArrayList<>();
    private int id = 1;

    private void validate(Film film) throws ValidationException {
        FilmValidation.validateName(film.getName());
        FilmValidation.validateDescription(film.getDescription());
        FilmValidation.validateDuration(film.getDuration());
        FilmValidation.validateReleaseDate(film.getReleaseDate());
    }

    private void addFilmToList(Film film) throws ValidationException {
        validate(film);
        film.setId(id);
        filmList.add(film);
        log.info("Фильм успешно добавлен");
        id++;
    }

    private void updateFilmList(Film film) throws ValidationException {
        if (filmList.contains(film)) {
            validate(film);
            filmList.remove(film);
            filmList.add(film);
            log.info("Фильм успешно обновлён");
        } else log.warn("Нужно сначала создать фильм");
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        addFilmToList(film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        updateFilmList(film);
        return film;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmList;
    }
}