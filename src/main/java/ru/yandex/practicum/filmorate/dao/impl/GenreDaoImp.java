package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;


@Component
@AllArgsConstructor
public class GenreDaoImp implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE ID = ?", id);
        if (sqlRowSet.next()) {
            Genre genre = new Genre();
            genre.setId(sqlRowSet.getLong("ID"));
            genre.setName(sqlRowSet.getString("NAME"));
            return genre;
        } else {
            throw new ExistingException("Такого жанра нет");
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM GENRES";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getLong("ID"));
            genre.setName(rs.getString("NAME"));
            return genre;
        });
        return genres;
    }
}