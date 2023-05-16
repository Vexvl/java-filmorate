package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet resultSet, int i) throws SQLException {

        Genre genre = new Genre();

        genre.setId(resultSet.getLong("ID"));
        genre.setName(resultSet.getString("NAME"));

        return genre;
    }
}
