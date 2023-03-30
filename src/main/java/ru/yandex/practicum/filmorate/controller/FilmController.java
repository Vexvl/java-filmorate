package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> filmMap= new HashMap<>();

    private int id = 1;

    private void validateReleaseDate(Film film) throws ValidationException {
        ValidationException.validateReleaseDate(film.getReleaseDate());
    }

    private void addFilmToList(Film film) throws ValidationException {
        validateReleaseDate(film);
        film.setId(id);
        filmMap.put(film.getId(), film);
        log.info("Фильм успешно добавлен");
        id++;
    }

    private void updateFilmList(Film film) throws ValidationException {
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
            log.info("Фильм успешно обновлён");
        } else throw new UpdateException("Такого фильма нет");
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
        return new ArrayList<Film>(filmMap.values());
    }

}