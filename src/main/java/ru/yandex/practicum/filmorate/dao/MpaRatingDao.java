package ru.yandex.practicum.filmorate.dao;

import java.util.List;

public interface MpaRatingDao {
    String getMpaRatingById(long id);

    List<String> getAllRatings();
}