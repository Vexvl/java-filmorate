package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {

    private final FilmStorageDao filmStorageDao;

    public FilmService(FilmStorageDao filmStorageDao) {
        this.filmStorageDao = filmStorageDao;
    }

    public Film createFilm(Film film) {
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
        filmStorageDao.addLike(filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        filmStorageDao.deleteLike(filmId, userId);
    }

    public List<Film> showTopFilms(int count) {
        List<Film> allFilms = filmStorageDao.showTopFilms();
        Comparator<Film> filmComparator = Comparator.comparingLong(film -> filmStorageDao.getLikedId(film.getId()).size());
        allFilms.sort(filmComparator.reversed());
        if (count >= allFilms.size()) {
            return allFilms;
        }
        return allFilms.subList(0, count);
    }
}