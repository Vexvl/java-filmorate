package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
import ru.yandex.practicum.filmorate.exceptions.ServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film createFilm(Film film) throws ServerException {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film getFilm(long id) {
        return filmStorage.getFilm(id);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void addLike(long filmId, long userId) {
        if (filmStorage.getFilm(filmId) == null || userStorage.getUser(userId) == null) {
            throw new ExistingException("Пользователь или фильм не существует");
        } else {
            Film film = filmStorage.getFilm(filmId);
            film.getLikedUsers().add(userId);
            filmStorage.update(film);
        }
    }

    public void deleteLike(long filmId, long userId) {
        if (filmStorage.getFilm(filmId) == null || userStorage.getUser(userId) == null) {
            throw new ExistingException("Пользователь или фильм не существует");
        }
        Film film = filmStorage.getFilm(filmId);
        film.getLikedUsers().remove(userId);
        filmStorage.update(film);
    }

    public List<Film> showTopFilms(int count) {
        Comparator<Film> film_comparator = Comparator.comparingInt(film -> film.getLikedUsers().size());
        return filmStorage.getFilms().stream()
                .sorted(film_comparator)
                .limit(count)
                .collect(Collectors.toList());
    }
}