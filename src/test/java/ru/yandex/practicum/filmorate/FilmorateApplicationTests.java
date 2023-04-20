package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class FilmorateApplicationTests {

    private FilmController filmController;
    private Film film;
    private UserController userController;
    private User user;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private FilmService filmService;
    private UserService userService;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController(userService);

        film = new Film();
        user = new User();

        film.setName("FilmName");
        film.setDescription("FilmDescription");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);
        user.setName("Username");
        user.setLogin("UserLogin");
        user.setBirthday(LocalDate.now());
        user.setEmail("user@mail.com");
    }

    @Test
    public void shouldValidateReleaseDate() {
        film.setReleaseDate(LocalDate.of(1885, 12, 28));

        final ValidationException exception = assertThrows(ValidationException.class, () -> filmController.createFilm(film));

        assertEquals("Дата релиза раньше дня рождения кино", exception.getMessage());
    }

    @Test
    public void shouldTValidateFilmUpdate() {
        film.setId(1);

        final ExistingException exception = assertThrows(ExistingException.class, () -> filmController.updateFilm(film));

        assertEquals("Фильма с id " + film.getId() + " нет", exception.getMessage());
    }

    @Test
    public void shouldValidateUserUpdate() {
        user.setId(1);

        final ExistingException exception = assertThrows(ExistingException.class, () -> userController.updateUser(user));

        assertEquals("Пользователя с id " + user.getId() + " нет", exception.getMessage());
    }

}