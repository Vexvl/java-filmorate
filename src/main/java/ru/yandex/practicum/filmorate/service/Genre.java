package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class Genre {
    @NotNull
    private int id;
    @NotBlank
    private String name;
}