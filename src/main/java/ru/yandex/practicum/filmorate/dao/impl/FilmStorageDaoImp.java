package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.dao.UserStorageDao;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FilmStorageDaoImp implements ru.yandex.practicum.filmorate.dao.FilmStorageDao {
    private final JdbcTemplate jdbcTemplate;

    private final MpaRatingDao mpaRatingDao;
    private final UserStorageDao userStorageDao;
    private final GenreDao genreDao;
    private long id = 0;

    @Autowired
    public FilmStorageDaoImp(JdbcTemplate jdbcTemplate, MpaRatingDao mpaRatingDao, UserStorageDao userStorageDao, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaRatingDao = mpaRatingDao;
        this.userStorageDao = userStorageDao;
        this.genreDao = genreDao;
    }

    @Override
    public Film create(Film film) {
        validateReleaseDate(film.getReleaseDate());
        id++;
        film.setId(id);
        List<Genre> genres = film.getGenres();
        if (genres != null && !genres.isEmpty()) {
            List<Long> genreIds = new ArrayList<>();
            for (Genre genre : genres) {
                genreIds.add(genre.getId());
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
            Film film = new Film();
            film.setId(sqlRowSet.getLong("FILM_ID"));
            film.setName(sqlRowSet.getString("NAME"));
            film.setDescription(sqlRowSet.getString("DESCRIPTION"));
            film.setReleaseDate(sqlRowSet.getDate("RELEASE_DATE").toLocalDate());
            film.setDuration(sqlRowSet.getInt("DURATION"));
            film.setMpa(mpaRatingDao.getMpaRatingById(sqlRowSet.getLong("MPA_RATING_ID")));

            List<Genre> genres = getGenresByFilmId(filmId);
            Set<Long> likes = new HashSet<>();

            sqlQuery = "SELECT USER_ID FROM FAVOURITE_FILMS WHERE FILM_ID = ?";
            SqlRowSet likesRows = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
            while (likesRows.next()) {
                likes.add(likesRows.getLong("USER_ID"));
            }
            film.setGenres(genres);
            film.setLikedUsers(likes);
            return film;
        } else {
            throw new ExistingException("Такого фильма нет");
        }
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT * FROM FILMS";
        List<Film> films = new ArrayList<>();

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery);
        while (filmRows.next()) {
            Film film = new Film();
            film.setId(filmRows.getLong("FILM_ID"));
            film.setName(filmRows.getString("NAME"));
            film.setDescription(filmRows.getString("DESCRIPTION"));
            film.setReleaseDate(filmRows.getDate("RELEASE_DATE").toLocalDate());
            film.setDuration(filmRows.getInt("DURATION"));
            film.setMpa(mpaRatingDao.getMpaRatingById(filmRows.getLong("MPA_RATING_ID")));

            long filmId = film.getId();
            List<Genre> genres = getGenresByFilmId(filmId);
            Set<Long> likes = new HashSet<>();

            String likesQuery = "SELECT USER_ID FROM FAVOURITE_FILMS WHERE FILM_ID = ?";
            SqlRowSet likesRows = jdbcTemplate.queryForRowSet(likesQuery, filmId);
            while (likesRows.next()) {
                likes.add(likesRows.getLong("USER_ID"));
            }

            film.setGenres(genres);
            film.setLikedUsers(likes);

            films.add(film);
        }

        return films;
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

    private void validateReleaseDate(LocalDate date) throws ValidationException {
        LocalDate validationDate = LocalDate.of(1895, 12, 28);
        if (date.isBefore(validationDate)) {
            log.warn("Дата релиза раньше дня рождения кино");
            throw new ValidationException("Дата релиза раньше дня рождения кино");
        }
    }

    private List<Genre> getGenresByFilmId(long filmId) {
        String sqlQuery = "SELECT G.* FROM GENRES G, FILM_GENRE FG WHERE G.ID = FG.GENRE_ID AND FG.FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, new Object[]{filmId}, new BeanPropertyRowMapper<>(Genre.class));
    }

}