package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

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
        return jdbcTemplate.query("SELECT * FROM GENRES WHERE ID=?", new Object[]{id}, new GenreMapper()).stream().findAny().orElseThrow();
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM GENRES", new GenreMapper());
    }
}