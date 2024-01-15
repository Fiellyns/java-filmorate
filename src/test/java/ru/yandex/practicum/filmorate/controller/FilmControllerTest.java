package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class FilmControllerTest {

    private final FilmStorage filmStorage = new InMemoryFilmStorage();
    private final UserStorage userStorage = new InMemoryUserStorage();
    private final FilmService filmService = new FilmService(filmStorage, userStorage);
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();
    private final Film film = Film.builder()
            .name("nisi eiusmod")
            .description("adipisicing")
            .releaseDate(LocalDate.parse("1895-12-28").plusMonths(1))
            .duration(100)
            .build();
    private FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController(filmService);
    }

    @Test
    void shouldCreateFilm() {
        filmController.createFilm(film);
        assertEquals(filmController.getAllFilms().size(), 1);
    }

    @Test
    void shouldCreateFilmWithFirstFilmDate() {
        Film filmWithFirstFilmDate = film
                .toBuilder()
                .releaseDate(LocalDate.parse("1895-12-28"))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(filmWithFirstFilmDate);

        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotCreateFilmIfNameIsEmpty() {
        String[] names = {"", " ", "  ", null};

        Arrays.stream(names).forEach(name -> {
            Film filmWithIncorrectName = film
                    .toBuilder()
                    .name(name)
                    .build();

            Set<ConstraintViolation<Film>> violations = validator.validate(filmWithIncorrectName);

            Assertions.assertFalse(violations.isEmpty());
        });
    }

    @Test
    void shouldNotCreateFilmIfDescriptionTooLong() {
        Film filmWithIncorrectDescription = film
                .toBuilder()
                .description("a".repeat(201))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(filmWithIncorrectDescription);

        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void shouldNotCreateFilmIfReleaseDateIsWrong() {
        Film filmWithIncorrectReleaseDate = film
                .toBuilder()
                .releaseDate(LocalDate.parse("1895-12-27"))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(filmWithIncorrectReleaseDate);

        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void shouldNotCreateFilmIfDurationIsWrong() {
        Film filmWithIncorrectDuration = film
                .toBuilder()
                .duration(-100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(filmWithIncorrectDuration);

        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
    }
}