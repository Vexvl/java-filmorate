package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.GenreDaoImp;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDaoImpTest {

    private final GenreDaoImp genreDao;

    @Test
    void getGenreById() {
        Genre genre = genreDao.getGenreById(1);
        assertEquals("Комедия", genre.getName());
    }

    @Test
    void getGenreByWrongId() {
        final NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> genreDao.getGenreById(-1));
        assertEquals("No value present", exception.getMessage());
    }

    @Test
    void getAllGenres() {
        List<Genre> genres = genreDao.getAllGenres();
        assertEquals(6, genres.size());
    }
}
