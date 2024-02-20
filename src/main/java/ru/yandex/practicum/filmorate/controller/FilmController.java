package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @GetMapping
    public List<Film> getAllFilms() {
        log.debug("Поступил GET-запрос для /films");
        return filmService.getFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.debug("Поступил POST-запрос для /films");
        return filmService.create(film);
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        log.debug("Поступил PUT-запрос для /films");
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable int id) {
        log.debug("Поступил GET-запрос для /films/{}", id);
        return filmService.findFilmById(id);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.debug("Поступил PUT-запрос для /films/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.debug("Поступил DELETE-запрос для /films/{}/like/{}", id, userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.debug("Поступил GET-запрос для /films/popular?count={}", count);
        return filmService.getMostPopularFilms(count);
    }
}
