package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
@AllArgsConstructor
public class MpaRatingController {

    private final MpaRatingService mpaRatingService;

    @GetMapping
    public List<MpaRating> getMpa() {
        return mpaRatingService.getMpa();
    }

    @GetMapping("/{id}")
    public MpaRating findMpaById(@PathVariable int id) {
        return mpaRatingService.findMpaById(id);
    }
}