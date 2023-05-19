package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmStorageDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilmService {

    private final FilmStorageDao filmStorageDao;

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
        Comparator<Film> comparator = Comparator.comparingLong(film -> filmStorageDao.getLikedId(film.getId()).size());
        return filmStorageDao.showTopFilms().stream()
                .sorted(comparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}