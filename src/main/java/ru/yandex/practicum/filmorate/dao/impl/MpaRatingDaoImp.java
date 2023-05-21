package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.exceptions.ExistingException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

@Component
@AllArgsConstructor
public class MpaRatingDaoImp implements MpaRatingDao {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public MpaRating getMpaRatingById(long id) {
        String sqlQuery = "SELECT * FROM MPA_RATING WHERE ID = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, new Object[]{id}, (rs, rowNum) -> {
                MpaRating mpaRating = new MpaRating();
                mpaRating.setId(rs.getLong("ID"));
                mpaRating.setName(rs.getString("NAME"));
                return mpaRating;
            });
        } catch (EmptyResultDataAccessException e) {
            throw new ExistingException("Такого рейтинга нет");
        }
    }

    @Override
    public List<MpaRating> getAllRatings() {
        String sqlQuery = "SELECT * FROM MPA_RATING";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            MpaRating rating = new MpaRating();
            rating.setId(rs.getLong("ID"));
            rating.setName(rs.getString("NAME"));
            return rating;
        });
    }

}