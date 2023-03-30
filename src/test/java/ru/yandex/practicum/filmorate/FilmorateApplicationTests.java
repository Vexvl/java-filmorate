package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class FilmorateApplicationTests {

    private FilmController filmController;
    private Film film;
    private UserController userController;
    private User user;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
        userController = new UserController();

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
    public void shouldTValidateFilmUpdate(){
        film.setId(1);

        final ValidationException exception = assertThrows(ValidationException.class, () -> filmController.updateFilm(film));

        assertEquals("Такого фильма нет", exception.getMessage());
    }

    @Test
    public void shouldValidateUserUpdate(){
        user.setId(1);

        final ValidationException exception = assertThrows(ValidationException.class, () -> userController.updateUser(user));

        assertEquals("Такого пользователя нет", exception.getMessage());
    }

}