package ru.yandex.practicum.filmorate.controller;

import org.apache.el.util.Validation;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FilmController {

    private void validate(Film film) throws ValidationException {
        FilmValidation.validateName(film.getName());
        FilmValidation.validateDescription(film.getDescription());
        FilmValidation.validateDuration(film.getDuration());
        FilmValidation.validateReleaseDate(film.getReleaseDate());
    }

    private List<Film> filmList = new ArrayList<>();

    @PostMapping("/film")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        filmList.add(film);
        validate(film);
        return film;
    }

    @PutMapping("/film")
    public List<Film> updateFilm(@RequestBody Film film) throws ValidationException {
        if (filmList.contains(film)) {
            filmList.remove(film);
            filmList.add(film);
            validate(film);
        }
        return filmList;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmList;
    }

}