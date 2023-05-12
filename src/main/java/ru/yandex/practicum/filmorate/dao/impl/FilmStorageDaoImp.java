package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmMapper;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class FilmStorageDaoImp implements ru.yandex.practicum.filmorate.dao.FilmStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public FilmStorageDaoImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        jdbcTemplate.update("INSERT INTO FILMS (FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING_ID) VALUES (?,?,?,?,?,?)", film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpaRating());
        return film;
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update("UPDATE FILMS SET FILM_ID=?, NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?, MPA_RATING_ID=? WHERE FILM_ID=?", film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpaRating(), film.getId());
        return film;
    }

    @Override
    public void delete(Film film) {
        jdbcTemplate.update("DELETE FROM FILMS WHERE FILM_ID=?", film.getId());
        log.info("Удален фильм: {} {}", film.getId(), film.getName());
    }

    @Override
    public Film getFilm(long filmId) {
        return jdbcTemplate.query("SELECT * FROM FILMS WHERE FILM_ID=?", new Object[]{filmId}, new FilmMapper()).stream().findAny().orElse(null);
    }

    @Override
    public List<Film> getFilms() {
        return jdbcTemplate.query("SELECT * FROM FILMS", new FilmMapper());
    }
}