package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FilmMapper implements RowMapper<Film> {

    private final MpaRatingDao mpaRatingDao;

    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;

    public FilmMapper(MpaRatingDao mpaRatingDao, JdbcTemplate jdbcTemplate, GenreDao genreDao) {
        this.mpaRatingDao = mpaRatingDao;
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        long filmId = rs.getLong("FILM_ID");

        String sqlQuery = "SELECT GENRE_ID FROM FILM_GENRE WHERE FILM_ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        List<Genre> genres = new ArrayList<>();
        while (genreRows.next()) {
            long genreId = genreRows.getLong("GENRE_ID");
            genres.add(genreDao.getGenreById(genreId));
        }

        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(rs.getInt("DURATION"));
        film.setMpa(mpaRatingDao.getMpaRatingById(rs.getLong("MPA_RATING_ID")));
        film.setGenres(genres);

        return film;
    }
}
