package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable(value = "id") long filmId, @PathVariable(value = "userId") long userId) {
        filmService.addLike(filmId, userId);
        log.info("Лайк фильму " + filmId + " поставлен");
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable(value = "id") long filmId, @PathVariable(value = "userId") long userId) {
        filmService.deleteLike(filmId, userId);
        log.info("Лайк фильму " + filmId + " удалён");
    }

    @GetMapping("/popular")
    public List<Film> returnTopFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.showTopFilms(count);
    }
}