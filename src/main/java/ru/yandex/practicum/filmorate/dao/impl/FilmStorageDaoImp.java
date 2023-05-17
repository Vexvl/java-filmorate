package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.dao.UserStorageDao;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FilmStorageDaoImp implements ru.yandex.practicum.filmorate.dao.FilmStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final MpaRatingDao mpaRatingDao;
    private final UserStorageDao userStorageDao;
    private final GenreDao genreDao;
    private long id = 0;


    public FilmStorageDaoImp(JdbcTemplate jdbcTemplate, MpaRatingDao mpaRatingDao, UserStorageDao userStorageDao, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaRatingDao = mpaRatingDao;
        this.userStorageDao = userStorageDao;
        this.genreDao = genreDao;
    }

    @Override
    public Film create(Film film) {
        ValidationException.validateReleaseDate(film.getReleaseDate());
        id++;
        film.setId(id);
        List<Genre> genres = film.getGenres();
        if (genres != null) {
            for (Genre genre : genres) {
                genreDao.getGenreById(genre.getId());
            }
        }
        jdbcTemplate.update("INSERT INTO FILMS (FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING_ID) VALUES (?,?,?,?,?,?)", film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        film.setGenres(updateGenres(genres, film.getId()));
        return film;
    }

    @Override
    public Film update(Film film) {
        getFilm(film.getId());
        List<Genre> genres = film.getGenres();
        film.setGenres(updateGenres(genres, film.getId()));
        jdbcTemplate.update("UPDATE FILMS SET NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?, MPA_RATING_ID=? WHERE FILM_ID=?", film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        getFilm(film.getId()).getLikedUsers().clear();
        getFilm(film.getId()).getLikedUsers().addAll(getLikedId(film.getId()));
        return film;
    }

    @Override
    public Film getFilm(long filmId) {
        String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = ?";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQuery, filmId);

        if (sqlRowSet.next()) {
            sqlQuery = "SELECT GENRE_ID FROM FILM_GENRE WHERE FILM_ID = ?";
            SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
            List<Genre> genres = new ArrayList<>();
            while (genreRows.next()) {
                long genreId = genreRows.getLong("GENRE_ID");
                genres.add(genreDao.getGenreById(genreId));
            }
            sqlQuery = "SELECT USER_ID FROM FAVOURITE_FILMS WHERE FILM_ID = ?";
            SqlRowSet likesRows = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
            Set<Long> likes = new HashSet<>();
            while (likesRows.next()) {
                likes.add(likesRows.getLong("USER_ID"));
            }
            Film film = new Film();
            film.setId(sqlRowSet.getLong("FILM_ID"));
            film.setName(sqlRowSet.getString("NAME"));
            film.setDescription(sqlRowSet.getString("DESCRIPTION"));
            film.setReleaseDate(sqlRowSet.getDate("RELEASE_DATE").toLocalDate());
            film.setDuration(sqlRowSet.getInt("DURATION"));
            film.setMpa(mpaRatingDao.getMpaRatingById(sqlRowSet.getLong("MPA_RATING_ID")));
            film.setGenres(genres);
            film.setLikedUsers(likes);

            return film;
        }
       else throw new ExistingException("Такого фильма нет");
    }

    @Override
    public List<Film> getFilms() {
        return jdbcTemplate.query("SELECT * FROM FILMS", new FilmMapper(mpaRatingDao, jdbcTemplate, genreDao));
    }

    @Override
    public void addLike(long filmId, long userId) {
        getFilm(filmId);
        userStorageDao.getUser(userId);
        jdbcTemplate.update("INSERT INTO FAVOURITE_FILMS (FILM_ID, USER_ID) VALUES (?, ?)", filmId, userId);
        getFilm(filmId).getLikedUsers().clear();
        getFilm(filmId).getLikedUsers().addAll(getLikedId(filmId));
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        getFilm(filmId);
        userStorageDao.getUser(userId);
        jdbcTemplate.update("DELETE FROM FAVOURITE_FILMS WHERE FILM_ID=? AND USER_ID=?", filmId, userId);
        getFilm(filmId).getLikedUsers().clear();
        getFilm(filmId).getLikedUsers().addAll(getLikedId(filmId));
    }

    @Override
    public List<Film> showTopFilms() {
        List<Film> films = new ArrayList<>();
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("SELECT FILM_ID FROM FILMS");
        while (filmsRows.next()) {
            films.add(getFilm(filmsRows.getLong("FILM_ID")));
        }
        Collections.sort(films, Comparator.comparingLong(Film::getId));
        return films;
    }

    private List<Genre> updateGenres(List<Genre> genres, Long filmId) {
        List<Genre> genresList = new ArrayList<>();
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
        if (genres != null && !genres.isEmpty()) {
            genres = genres.stream().distinct().collect(Collectors.toList());
            for (Genre genre : genres) {
                sqlQuery = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
                jdbcTemplate.update(sqlQuery, filmId, genre.getId());
                genre = genreDao.getGenreById(genre.getId());
                genresList.add(genre);
            }
        }
        return genresList;
    }
    @Override
    public List<Long> getLikedId(long filmId) {
        List<Long> likes = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT FILM_ID FROM FAVOURITE_FILMS WHERE FILM_ID=?", filmId);
        while (sqlRowSet.next()) {
            likes.add(sqlRowSet.getLong("FILM_ID"));
        }
        return likes;
    }
}