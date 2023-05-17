package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorageDao {
    Film create(Film film);

    Film update(Film film);

    Film getFilm(long id);

    List<Film> getFilms();

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Film> showTopFilms();

    List<Long> getLikedId(long filmId);
}