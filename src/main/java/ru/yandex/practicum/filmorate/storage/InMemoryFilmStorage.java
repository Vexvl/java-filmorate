package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> filmMap = new HashMap<>();

    private long id = 1;

    @Override
    public Film create(Film film) {
        ValidationException.validateReleaseDate(film.getReleaseDate());
        film.setId(id);
        filmMap.put(film.getId(), film);
        id++;
        log.info("Фильм с id " + film.getId() + " успешно добавлен");
        return film;
    }

    @Override
    public Film update(Film film) {
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
            log.info("Фильм с id " + film.getId() + " успешно обновлён");
            return film;
        } else {
            throw new ExistingException("Фильма с id " + film.getId() + " нет");
        }
    }

    @Override
    public Film delete(Film film) {
        if (filmMap.containsValue(film)) {
            filmMap.remove(film);
            return film;
        } else {
            throw new ExistingException("Такого фильма нет");
        }
    }

    @Override
    public Film getFilm(long id) {
        if (filmMap.containsKey(id)) {
            return filmMap.get(id);
        } else {
            throw new ExistingException("Такого фильма нет");
        }
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(filmMap.values());
    }
}