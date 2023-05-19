package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreDao genreDao;

    public Genre getGenreById(int id) {
        return genreDao.getGenreById(id);
    }

    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }
}