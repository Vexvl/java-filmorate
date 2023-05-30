package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorageDao;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FilmStorageDaoImp implements ru.yandex.practicum.filmorate.dao.FilmStorageDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorageDao userStorageDao;
    private long id = 0;

    @Autowired
    public FilmStorageDaoImp(JdbcTemplate jdbcTemplate, UserStorageDao userStorageDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorageDao = userStorageDao;
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
        String sqlQuery = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_RATING_ID, mr.NAME AS MPA_RATING_NAME, g.ID, g.NAME AS GENRE_NAME, ff.USER_ID " +
                "FROM FILMS f " +
                "LEFT JOIN FILM_GENRE fg ON f.FILM_ID = fg.FILM_ID " +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.ID " +
                "LEFT JOIN FAVOURITE_FILMS ff ON f.FILM_ID = ff.FILM_ID " +
                "LEFT JOIN MPA_RATING mr ON f.MPA_RATING_ID = mr.ID " +
                "WHERE f.FILM_ID = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        Film film = null;

        while (rowSet.next()) {
            if (film == null) {
                film = new Film();
                film.setId(rowSet.getLong("FILM_ID"));
                film.setName(rowSet.getString("NAME"));
                film.setDescription(rowSet.getString("DESCRIPTION"));
                film.setReleaseDate(rowSet.getDate("RELEASE_DATE").toLocalDate());
                film.setDuration(rowSet.getInt("DURATION"));
                film.setGenres(new ArrayList<>());
                film.setLikedUsers(new HashSet<>());
                MpaRating mpaRating = new MpaRating();
                mpaRating.setId(rowSet.getLong("MPA_RATING_ID"));
                mpaRating.setName(rowSet.getString("MPA_RATING_NAME"));
                film.setMpa(mpaRating);
            }

            Long genreId = rowSet.getLong("ID");
            if (!rowSet.wasNull()) {
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(rowSet.getString("GENRE_NAME"));
                film.getGenres().add(genre);
            }

            Long userId = rowSet.getLong("USER_ID");
            if (!rowSet.wasNull()) {
                film.getLikedUsers().add(userId);
            }
        }

        if (film != null) {
            return film;
        } else {
            throw new ExistingException("Такого фильма нет");
        }
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_RATING_ID, mr.NAME AS MPA_RATING_NAME, g.ID, g.NAME AS GENRE_NAME, ff.USER_ID " +
                "FROM FILMS f " +
                "LEFT JOIN FILM_GENRE fg ON f.FILM_ID = fg.FILM_ID " +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.ID " +
                "LEFT JOIN FAVOURITE_FILMS ff ON f.FILM_ID = ff.FILM_ID " +
                "LEFT JOIN MPA_RATING mr ON f.MPA_RATING_ID = mr.ID ";

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery);

        Map<Long, Film> filmMap = new HashMap<>();

        while (filmRows.next()) {
            long filmId = filmRows.getLong("FILM_ID");
            Film film = filmMap.get(filmId);
            if (film == null) {
                film = new Film();
                film.setId(filmRows.getLong("FILM_ID"));
                film.setName(filmRows.getString("NAME"));
                film.setDescription(filmRows.getString("DESCRIPTION"));
                film.setReleaseDate(filmRows.getDate("RELEASE_DATE").toLocalDate());
                film.setDuration(filmRows.getInt("DURATION"));
                film.setGenres(new ArrayList<>());
                film.setLikedUsers(new HashSet<>());
                MpaRating mpaRating = new MpaRating();
                mpaRating.setId(filmRows.getLong("MPA_RATING_ID"));
                mpaRating.setName(filmRows.getString("MPA_RATING_NAME"));
                film.setMpa(mpaRating);
                filmMap.put(filmId, film);
            }

            Long genreId = filmRows.getLong("ID");
            if (!filmRows.wasNull()) {
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(filmRows.getString("GENRE_NAME"));
                film.getGenres().add(genre);
            }

            Long userId = filmRows.getLong("USER_ID");
            if (!filmRows.wasNull()) {
                film.getLikedUsers().add(userId);
            }
        }
        return new ArrayList<>(filmMap.values());
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
        String sqlQuery = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_RATING_ID, mr.NAME AS MPA_RATING_NAME, mr.ID AS MPA_RATING_ID, g.ID, g.NAME AS GENRE_NAME, ff.USER_ID " +
                "FROM FILMS f " +
                "LEFT JOIN FILM_GENRE fg ON f.FILM_ID = fg.FILM_ID " +
                "LEFT JOIN GENRES g ON fg.GENRE_ID = g.ID " +
                "LEFT JOIN FAVOURITE_FILMS ff ON f.FILM_ID = ff.FILM_ID " +
                "LEFT JOIN MPA_RATING mr ON f.MPA_RATING_ID = mr.ID";

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery);
        Map<Long, Film> filmMap = new HashMap<>();

        while (filmRows.next()) {
            long filmId = filmRows.getLong("FILM_ID");
            Film film = filmMap.get(filmId);

            if (film == null) {
                film = new Film();
                film.setId(filmRows.getLong("FILM_ID"));
                film.setName(filmRows.getString("NAME"));
                film.setDescription(filmRows.getString("DESCRIPTION"));
                film.setReleaseDate(filmRows.getDate("RELEASE_DATE").toLocalDate());
                film.setDuration(filmRows.getInt("DURATION"));
                film.setGenres(new ArrayList<>());
                film.setLikedUsers(new HashSet<>());
                MpaRating mpaRating = new MpaRating();
                mpaRating.setId(filmRows.getLong("MPA_RATING_ID"));
                mpaRating.setName(filmRows.getString("MPA_RATING_NAME"));
                film.setMpa(mpaRating);
                filmMap.put(filmId, film);
            }

            Long genreId = filmRows.getLong("ID");
            if (!filmRows.wasNull()) {
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(filmRows.getString("GENRE_NAME"));
                film.getGenres().add(genre);
            }

            Long userId = filmRows.getLong("USER_ID");
            if (!filmRows.wasNull()) {
                film.getLikedUsers().add(userId);
            }
        }
        return new ArrayList<>(filmMap.values());
    }

    private List<Genre> updateGenres(List<Genre> genres, Long filmId) {
        List<Genre> genresList = new ArrayList<>();

        String deleteQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(deleteQuery, filmId);

        if (genres != null && !genres.isEmpty()) {
            genres = genres.stream().distinct().collect(Collectors.toList());

            String insertQuery = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";

            List<Object[]> batchArgs = new ArrayList<>();
            for (Genre genre : genres) {
                batchArgs.add(new Object[]{filmId, genre.getId()});
            }

            jdbcTemplate.batchUpdate(insertQuery, batchArgs);

            String selectQuery = "SELECT g.ID, g.NAME FROM GENRES g INNER JOIN FILM_GENRE fg ON g.ID = fg.GENRE_ID WHERE fg.FILM_ID = ?";
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(selectQuery, filmId);

            while (rowSet.next()) {
                Genre genre = new Genre();
                genre.setId(rowSet.getLong("id"));
                genre.setName(rowSet.getString("name"));
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
}