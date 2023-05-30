package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.MpaRatingDaoImp;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaRatingDaoImpTest {

    private final MpaRatingDaoImp mpaRatingDao;

    @Test
    void getGenreById() {
        MpaRating rating = mpaRatingDao.getMpaRatingById(1);
        assertEquals("G", rating.getName());
    }

    @Test
    void getGenreByWrongId() {
        final NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> mpaRatingDao.getMpaRatingById(-1));
        assertEquals("No value present", exception.getMessage());
    }

    @Test
    void getAllGenres() {
        List<MpaRating> ratings = mpaRatingDao.getAllRatings();
        assertEquals(5, ratings.size());
    }
}