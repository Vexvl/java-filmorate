package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmMapper;
import ru.yandex.practicum.filmorate.service.GenreMapper;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class GenreDaoImp implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(long id) {
        return jdbcTemplate.query("SELECT * FROM GENRES WHERE ID=?", new Object[]{id}, new GenreMapper()).stream().findAny().orElse(null);
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM GENRES", new GenreMapper());
    }
}
