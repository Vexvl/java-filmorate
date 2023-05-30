package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

@Service
@AllArgsConstructor
public class MpaRatingService {

    private final MpaRatingDao mpaRatingDao;

    public MpaRating findMpaById(int id) {
        return mpaRatingDao.getMpaRatingById(id);
    }

    public List<MpaRating> getMpa() {
        return mpaRatingDao.getAllRatings();
    }
}