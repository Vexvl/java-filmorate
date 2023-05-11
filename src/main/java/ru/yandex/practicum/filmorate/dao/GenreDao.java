package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.service.Genre;

import java.util.List;

public interface GenreDao {
    Genre getById(long id);

    List<Genre> getAll();
}
