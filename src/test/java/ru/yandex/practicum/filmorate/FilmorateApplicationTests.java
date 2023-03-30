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
        film = new Film();
        film.setName("FilmName");
        film.setDescription("FilmDescription");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);

        userController = new UserController();
        user = new User();
        user.setName("Username");
        user.setLogin("UserLogin");
        user.setBirthday(LocalDate.now());
        user.setEmail("user@mail.com");
    }

    @Test
    public void shouldValidateName() {

        film.setName(null);

        final ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));

        assertEquals("Имя не должно быть пустым", exception.getMessage());
    }

    @Test
    public void shouldValidateReleaseDate() {
        film.setReleaseDate(LocalDate.of(1885, 12, 28));

        final ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));

        assertEquals("Дата релиза раньше дня рождения кино", exception.getMessage());
    }

    @Test
    public void shouldValidateDescription() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1234567890".repeat(21));
        film.setDescription(stringBuilder.toString());

        final ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));

        assertEquals("Описание должно быть короче 200 символов", exception.getMessage());
    }


    @Test
    public void shouldValidateDuration() {

        film.setDuration(-3);

        final ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));

        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    public void shouldValidateLoginNull() {

        user.setLogin(null);

        final ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));

        assertEquals("Логин не должнен быть пустым", exception.getMessage());
    }

    @Test
    public void shouldValidateLoginSpaces() {

        user.setLogin("l o g i n");

        final ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));

        assertEquals("Логин содержит пробелы", exception.getMessage());
    }

    @Test
    public void shouldValidateBirthDay() {

        LocalDate birthday = LocalDate.of(3000, 12, 1);

        user.setBirthday(birthday);

        final ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));

        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

}