package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.service.MPA_Rating;

import java.util.List;

public interface MpaRatingDao {
    MPA_Rating getById(long id);

    List<MPA_Rating> getAll();
}