package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private Film film;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(long filmId, long userId) {
        film = filmStorage.getFilm(filmId);
        film.getLikedUsers().add(userId);
        filmStorage.update(film);
    }

    public void deleteLike(long filmId, long userId) {
        if (!userStorage.getUsers().contains(userStorage.getUser(userId))) {
            throw new ExistingException("Такого пользователя нет");
        }
        film = filmStorage.getFilm(filmId);
        film.getLikedUsers().remove(userId);
        filmStorage.update(film);
    }

    public List<Film> showTopFilms(int count) {
        List<Film> films = filmStorage.getFilms();
        int endIndex = Math.min(count, films.size()); // Если count больше, чем количество фильмов, берем все фильмы
        if (films.isEmpty()) {
            return new ArrayList<>();
        }
        Comparator<Film> comparator = Comparator.comparingInt(film -> film.getLikedUsers().size());
        films.sort(comparator);
        return films.subList(0, endIndex);
    }
}