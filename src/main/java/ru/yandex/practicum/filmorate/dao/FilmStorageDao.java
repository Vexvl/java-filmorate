package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorageDao {
    Film create(Film film);

    Film update(Film film);

    void delete(Film film);

    Film getFilm(long id);

    List<Film> getFilms();
}
