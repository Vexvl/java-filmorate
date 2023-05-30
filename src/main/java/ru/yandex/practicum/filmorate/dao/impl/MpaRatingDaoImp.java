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
        String sqlQuery = "SELECT * FROM MPA_RATING WHERE ID = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (rowSet.next()) {
            MpaRating mpaRating = new MpaRating();
            mpaRating.setId(rowSet.getLong("ID"));
            mpaRating.setName(rowSet.getString("NAME"));
            return mpaRating;
        } else {
            throw new ExistingException("Такого рейтинга нет");
        }
    }

    @Override
    public List<MpaRating> getAllRatings() {
        String sqlQuery = "SELECT * FROM MPA_RATING";
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(sqlQuery);
        List<MpaRating> ratings = new ArrayList<>();

        while (ratingRows.next()) {
            MpaRating rating = new MpaRating();
            rating.setId(ratingRows.getLong("ID"));
            rating.setName(ratingRows.getString("NAME"));
            ratings.add(rating);
        }

        return ratings;
    }

}