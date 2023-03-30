package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final List<Film> filmList = new ArrayList<>();

    private int id = 1;

    private void validateReleaseDate(Film film) throws ValidationException {
        ValidationException.validateReleaseDate(film.getReleaseDate());
    }

    private void addFilmToList(Film film) throws ValidationException {
        validateReleaseDate(film);
        film.setId(id);
        filmList.add(film);
        log.info("Фильм успешно добавлен");
        id++;
    }

    private void updateFilmList(Film film) throws ValidationException {
        if (filmList.contains(film)) {
            validateReleaseDate(film);
            filmList.remove(film);
            film.setId(id++);
            filmList.add(film);
            log.info("Фильм успешно обновлён");
        } else
            filmList.add(film);
            log.info("Такого фильма нет, он был создан");
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) throws ValidationException {
        addFilmToList(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        updateFilmList(film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmList;
    }

}