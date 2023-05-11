package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class MPA_Rating {
    @NotNull
    private int id;
    @NotBlank
    private String name;
}