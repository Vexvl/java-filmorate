package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmStorageDao;

import ru.yandex.practicum.filmorate.dao.UserStorageDao;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private long id = 0;

    private final FilmStorageDao filmStorageDao;
    private final UserStorageDao userStorageDao;

    public FilmService(FilmStorageDao filmStorageDao, UserStorageDao userStorageDao) {
        this.filmStorageDao = filmStorageDao;
        this.userStorageDao = userStorageDao;
    }

    public Film createFilm(Film film) {
        film.setId(+1);
        return filmStorageDao.create(film);
    }

    public Film update(Film film) {
        return filmStorageDao.update(film);
    }

    public Film getFilm(long id) {
        return filmStorageDao.getFilm(id);
    }

    public List<Film> getFilms() {
        return filmStorageDao.getFilms();
    }

    public void addLike(long filmId, long userId) {
        if (filmStorageDao.getFilm(filmId) == null || userStorageDao.getUser(userId) == null) {
            throw new ExistingException("Пользователь или фильм не существует");
        } else {
            Film film = filmStorageDao.getFilm(filmId);
            film.getLikedUsers().add(userId);
            filmStorageDao.update(film);
        }
    }

    public void deleteLike(long filmId, long userId) {
        if (filmStorageDao.getFilm(filmId) == null || userStorageDao.getUser(userId) == null) {
            throw new ExistingException("Пользователь или фильм не существует");
        }
        Film film = filmStorageDao.getFilm(filmId);
        film.getLikedUsers().remove(userId);
        filmStorageDao.update(film);
    }

    public List<Film> showTopFilms(int count) {
        Comparator<Film> comparator = Comparator.comparingInt(film -> film.getLikedUsers().size());
        return filmStorageDao.getFilms().stream()
                .sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());
    }
}