package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class MpaRatingDaoImp implements MpaRatingDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public MpaRating getMpaRatingById(long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM MPA_RATING WHERE ID = ?", id);
        if (sqlRowSet.next()) {
            MpaRating mpaRating = new MpaRating();

            mpaRating.setId(sqlRowSet.getLong("ID"));
            mpaRating.setName(sqlRowSet.getString("NAME"));

            return mpaRating;
        } else {
            throw new ExistingException("Такого рейтинга нет");
        }
    }

    @Override
    public List<MpaRating> getAllRatings() {
        List<MpaRating> ratings = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM MPA_RATING");
        while (rowSet.next()) {
            MpaRating rating = new MpaRating();
            rating.setId(rowSet.getLong("ID"));
            rating.setName(rowSet.getString("NAME"));
            ratings.add(rating);
        }

        return ratings;
    }
}