package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MpaRatingMapper implements RowMapper<MpaRating> {
    @Override
    public MpaRating mapRow(ResultSet resultSet, int i) throws SQLException {

        MpaRating mpaRating = new MpaRating();

        mpaRating.setId(resultSet.getLong("ID"));
        mpaRating.setName(resultSet.getString("NAME"));

        return mpaRating;
    }
}
