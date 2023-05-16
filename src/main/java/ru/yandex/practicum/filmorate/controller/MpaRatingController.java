package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaRatingController {
    private final MpaRatingDao mpaRatingDao;

    public MpaRatingController(MpaRatingDao mpaRatingDao) {
        this.mpaRatingDao = mpaRatingDao;
    }

    @GetMapping
    public List<MpaRating> getMpa() {
        return mpaRatingDao.getAllRatings();
    }

    @GetMapping("/{id}")
    public MpaRating findMpaById(@PathVariable int id) {
        return mpaRatingDao.getMpaRatingById(id);
    }
}
