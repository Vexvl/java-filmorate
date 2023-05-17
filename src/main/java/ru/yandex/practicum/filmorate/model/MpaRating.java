package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MpaRating {
    @NotNull
    private long id;
    @NotBlank
    private String name;
}