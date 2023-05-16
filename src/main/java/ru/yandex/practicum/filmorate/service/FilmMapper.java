package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dao.impl.GenreDaoImp;
import ru.yandex.practicum.filmorate.dao.impl.MpaRatingDaoImp;
import ru.yandex.practicum.filmorate.model.Film;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FilmMapper implements RowMapper<Film> {

    private MpaRatingDaoImp mpaRatingDaoImp;
    private GenreDaoImp genreDaoImp;

    @Override
    public Film mapRow(ResultSet resultSet, int i) throws SQLException {

        Film film = new Film();

        film.setId(resultSet.getLong("FILM_ID"));
        film.setName(resultSet.getString("NAME"));
        film.setDescription(resultSet.getString("DESCRIPTION"));
        film.setDuration(resultSet.getInt("DURATION"));
        film.setReleaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate());
        film.setMpa(mpaRatingDaoImp.getMpaRatingById(resultSet.getLong("MPA_RATING_ID")));

        String genresString = resultSet.getString("GENRES");
        List<String> genresList = Arrays.asList(genresString.split(","));
        List<Genre> genres = new ArrayList<>();
        for (String string : genresList) {
            int id = Integer.parseInt(string);
            genres.add(genreDaoImp.getGenreById(id));
        }
        film.setGenres(genres);
        return film;
    }
}