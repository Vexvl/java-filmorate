package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.mapper.MpaRatingMapper;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

@Component
@AllArgsConstructor
public class MpaRatingDaoImp implements MpaRatingDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public MpaRating getMpaRatingById(long id) {
        return jdbcTemplate.query("SELECT * FROM MPA_RATING WHERE ID=?", new Object[]{id}, new MpaRatingMapper()).stream().findAny().orElseThrow();
    }

    @Override
    public List<MpaRating> getAllRatings() {
        return jdbcTemplate.query("SELECT * FROM MPA_RATING", new MpaRatingMapper());
    }
}