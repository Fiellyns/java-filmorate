package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping ("/films")
public class FilmController {
    private final Map<Integer,Film> films = new HashMap<>();

    private int idForFilm = 0;

    public int idGeneration() {
        return ++idForFilm;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        film.setId(idGeneration());
        log.info("Добавлен новый фильм.");
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())){
                log.warn("Произошла замена ID");
                throw new ValidationException("ID не может быть изменён.");
            } else {
            if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
                log.warn("Введена неверная дата фильма.");
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
            }
            log.info("Фильм с id: " + film.getId() + " был обновлен.");
            films.put(film.getId(), film);
            return film;
        }
    }
}
