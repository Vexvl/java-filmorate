package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaRatingDao {
    MpaRating getMpaRatingById(long id);

    List<MpaRating> getAllRatings();
}