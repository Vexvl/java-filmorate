package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.FilmMapper;
import ru.yandex.practicum.filmorate.service.MpaRatingMapper;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MpaRatingDaoImp implements MpaRatingDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDaoImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MpaRating getMpaRatingById(long id) {
        return jdbcTemplate.query("SELECT * FROM MPA_RATING WHERE ID=?", new Object[]{id}, new MpaRatingMapper()).stream().findAny().orElse(null);
    }

    @Override
    public List<MpaRating> getAllRatings() {
        return jdbcTemplate.query("SELECT * FROM MPA_RATING", new MpaRatingMapper());
    }
}
