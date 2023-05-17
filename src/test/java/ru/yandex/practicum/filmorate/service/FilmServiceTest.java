package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmServiceTest {

    private final FilmService filmService;
    private final UserService userService;

    Film film = new Film();
    MpaRating mpa = new MpaRating();

    @BeforeEach
    public void go() {
        mpa.setId(1);
        film.setId(1);
        film.setName("name");
        film.setDescription("des");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(100);
        film.setMpa(mpa);
    }

    @Test
    public void testCreateFilmMustReturnUserWithNewId() {
        film = filmService.createFilm(film);
        assertNotEquals(-1, film.getId());
    }

    @Test
    void testGetFilm(){
        film = filmService.createFilm(film);
        assertNotNull(filmService.getFilm(film.getId()));
    }

    @Test
    public void testCreateFilmWithWrongParameters() {
        film.setMpa(null);
        final NullPointerException exceptionMpa = assertThrows(NullPointerException.class, () -> filmService.createFilm(film));
        assertNotNull("null", exceptionMpa.getMessage());
    }

    @Test
    public void testGetFilmsWithNotEmptyBase() {
        filmService.createFilm(film);
        List<Film> films = filmService.getFilms();
        assertFalse(films.isEmpty());
    }

    @Test
    public void testFindUserByNormalId() {
        Film expectedFilm = filmService.createFilm(film);
        assertEquals(expectedFilm.getName(), film.getName());
    }

    @Test
    public void testFindUserByWrongId() {
        final NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> filmService.getFilm(-1));
        assertEquals("No value present", exception.getMessage());
    }

    @Test
    public void testUpdateFilmWithWrongId() {
        final NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> filmService.update(film));
        assertEquals("No value present", exception.getMessage());
    }

    @Test
    public void testUpdateFilmNormalId() {
        film.setName("testUpdateFilmNormalId");
        film = filmService.createFilm(film);
        MpaRating mpaRating = new MpaRating();
        mpaRating.setId(2);
        film.setMpa(mpaRating);
        filmService.update(film);
        Film expectedFilm = filmService.getFilm(film.getId());
        assertEquals(2, expectedFilm.getMpa().getId());
    }

    @Test
    void testAddLike() {
        film = filmService.createFilm(film);
        User test = new User();
        test.setName("user");
        test.setEmail("email@yandex.ru");
        test.setLogin("login");
        test.setBirthday(LocalDate.now());
        userService.createUser(test);
        filmService.addLike(1, 1);
        film = filmService.getFilm(film.getId());
        assertEquals(1, film.getLikedUsers().size());
    }

    @Test
    void testDeleteLike() {
        film = filmService.createFilm(film);
        User test = new User();
        test.setName("user");
        test.setEmail("email@yandex.ru");
        test.setLogin("login");
        test.setBirthday(LocalDate.now());
        userService.createUser(test);
        filmService.addLike(film.getId(), test.getId());
        filmService.deleteLike(film.getId(), test.getId());
        assertEquals(0,film.getLikedUsers().size());
    }

    @Test
    void testGetPopularFilms() {
        film = filmService.createFilm(film);
        User test = new User();
        test.setName("user");
        test.setEmail("email@yandex.ru");
        test.setLogin("login");
        test.setBirthday(LocalDate.now());
        userService.createUser(test);
        filmService.addLike(film.getId(), test.getId());
        film.setId(2);
        film.setName("Name2");
        filmService.createFilm(film);
        List<Film> films = filmService.showTopFilms(1);
        assertEquals(1, films.size());
    }

}