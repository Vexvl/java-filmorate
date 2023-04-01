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

    private final Map<Long, Film> filmMap = new HashMap<>();

    private long id = 1;

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        addFilmToList(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        updateFilmList(film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<Film>(filmMap.values());
    }

    private void addFilmToList(Film film) {
        ValidationException.validateReleaseDate(film.getReleaseDate());
        film.setId(id);
        filmMap.put(film.getId(), film);
        log.info("Фильм с id " + film.getId() + "успешно добавлен");
        id++;
    }

    private void updateFilmList(Film film) {
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
            log.info("Фильм с id " + film.getId() + "успешно обновлён");
        } else {
            throw new UpdateException("Фильма с id " + film.getId() + " нет");
        }
    }

}