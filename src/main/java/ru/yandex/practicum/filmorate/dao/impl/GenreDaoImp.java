package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;


@Component
@AllArgsConstructor
public class GenreDaoImp implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(long id) {
        return jdbcTemplate.query("SELECT * FROM GENRES WHERE ID=?", new Object[]{id}, new GenreMapper()).stream().findAny().orElseThrow();
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM GENRES", new GenreMapper());
    }
}