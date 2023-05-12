package ru.yandex.practicum.filmorate.dao;

import java.util.List;

public interface GenreDao {
    String getGenreById(long id);

    List<String> getAllGenres();
}
