package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;


public class MpaRatingMapper implements RowMapper<MpaRating> {

    @Override
    public MpaRating mapRow(ResultSet resultSet, int i) throws SQLException {

        MpaRating mpaRating = new MpaRating();

        mpaRating.setId(resultSet.getLong("ID"));
        mpaRating.setName(resultSet.getString("NAME"));

        return mpaRating;
    }
}