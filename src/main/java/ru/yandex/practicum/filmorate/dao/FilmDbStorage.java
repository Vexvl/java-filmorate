package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.exception.StorageException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Genre;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaRatingDao mpaRatingDao;

    private final GenreDao genreDao;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaRatingDao mpaRatingDao, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaRatingDao = mpaRatingDao;
        this.genreDao = genreDao;
    }


    @Override
    public Film create(Film film) {
        findMatch(film);
        int mpaId = film.getMpa().getId();
        film.setMpa(mpaRatingDao.findById(mpaId));

        List<Genre> genres = film.getGenres();
        if (genres != null) {
            for (Genre genre : genres) {
                genreDao.findById(genre.getId());
            }
        }

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId((int) simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());

        film.setGenres(updateGenres(genres, film.getId()));

        log.info("Создан фильм: {} {}.", film.getId(), film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "SELECT * FROM films WHERE film_id = ?";

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, film.getId());

        if (filmRows.next()) {
            sqlQuery = "UPDATE films " + "SET name= ?, " + "description = ?, " + "release_date = ?, " + "duration = ?, " + "mpa_rating_id = ? " +
                    "WHERE film_id = ?;";

            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());

            List<Genre> genres = film.getGenres();
            film.setGenres(updateGenres(genres, film.getId()));

            List<Integer> likes = film.getLikes();
            film.setLikes(updateLikes(likes, film.getId()));

            log.info("Обновлен фильм: {} {}", film.getId(), film.getName());

            return film;
        } else {
            log.warn("Фильм с идентификатором {} не найден.", film.getId());
            throw new StorageException(String.format("Фильм с идентификатором %d не найден.", film.getId()));
        }
    }

    @Override
    public void delete(Film film) {
        String sqlQuery = "DELETE FROM films " + "WHERE film_id = ?;";
        jdbcTemplate.update(sqlQuery, film.getId());
        log.info("Удален фильм: {} {}", film.getId(), film.getName());
    }

    @Override
    public Film getFilm(long filmId) {

        String sqlQuery = "SELECT * FROM films WHERE film_id = ?";

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, filmId);

        if (filmRows.next()) {
            sqlQuery = "SELECT genre_id FROM film_genre WHERE film_id = ?";
            SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, filmId);

            List<Genre> genres = new ArrayList<>();
            while (genreRows.next()) {
                int genreId = Integer.parseInt(genreRows.getString("GENRE_ID"));
                genres.add(genreDao.findById(genreId));
            }

            sqlQuery = "SELECT user_id FROM favorite_films WHERE film_id = ?";
            SqlRowSet likesRows = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
            List<Integer> likes = new ArrayList<>();
            while (likesRows.next()) {
                likes.add(Integer.parseInt(likesRows.getString("USER_ID")));
            }

            Film film = new Film(
                    Integer.parseInt(filmRows.getString("FILM_ID")),
                    filmRows.getString("NAME").trim(),
                    filmRows.getString("DESCRIPTION").trim(),
                    LocalDate.parse(filmRows.getString("RELEASE_DATE").trim(), formatter),
                    Integer.parseInt(filmRows.getString("DURATION")),
                    likes,
                    genres,
                    mpaRatingDao.findById(Integer.parseInt(filmRows.getString("MPA_RATING_ID"))));

            log.info("Найден фильм: {} {}", film.getId(), film.getName());

            return film;
        } else {
            log.warn("Фильм с идентификатором {} не найден.", filmId);
            throw new StorageException(String.format("Фильм с идентификатором %d не найден.", filmId));
        }
    }
}