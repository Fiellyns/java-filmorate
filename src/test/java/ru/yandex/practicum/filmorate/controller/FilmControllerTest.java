package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.dao.like.LikeStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {

    private final FilmService filmService;
    private final UserService userService;
    private final LikeStorage likeStorage;
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    private final Film newFilm = Film.builder()
            .name("rudsruew")
            .description("urdut")
            .duration(120)
            .releaseDate(LocalDate.now())
            .mpa(new Mpa(1, "G"))
            .build();

    private final User user = User.builder()
            .login("a")
            .name("nisi eiusmod")
            .email("gh1u@mail.ru")
            .birthday(LocalDate.now())
            .build();

    @Test
    public void shouldCreateFilm() {
        Film film = filmService.create(newFilm);
        System.out.println(filmService.getFilms());
        System.out.println(film);
        assertEquals("rudsruew", filmService.findFilmById(film.getId()).getName());
    }

    @Test
    public void shouldNotPassNameValidation() {
        Film film = Film.builder()
                .name("")
                .description("urdut")
                .duration(120)
                .releaseDate(LocalDate.now())
                .mpa(new Mpa(1, "G"))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(2, violations.size());
    }

    @Test
    public void shouldNotPassDescriptionValidation() {
        Film film = Film.builder()
                .name("rudsruew")
                .description("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                .duration(120)
                .releaseDate(LocalDate.now())
                .mpa(new Mpa(1, "G"))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }

    @Test
    public void shouldNotPassReleaseDateValidationInThePast() {
        Film film1 = Film.builder()
                .name("rudsruew")
                .description("urdut")
                .duration(120)
                .releaseDate(LocalDate.of(1722, 12, 6))
                .mpa(new Mpa(1, "PG"))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertEquals(1, violations.size());
    }

    @Test
    public void shouldPassReleaseDateValidationInTheFuture() {
        Film film = Film.builder()
                .name("rudsruew")
                .description("urdut")
                .duration(120)
                .releaseDate(LocalDate.of(2025, 12, 6))
                .mpa(new Mpa(1, "PG"))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());
    }

    @Test
    public void shouldNotPassDurationValidation() {
        Film film = Film.builder()
                .name("rudsruew")
                .description("urdut")
                .duration(-120)
                .releaseDate(LocalDate.now())
                .mpa(new Mpa(1, "PG"))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }

    @Test
    public void shouldUpdateFilm() {
        Film film = filmService.create(newFilm);

        Film filmUpdate = Film.builder()
                .id(film.getId())
                .name("Film")
                .description("film_film")
                .duration(120)
                .releaseDate(LocalDate.of(2018, 11, 21))
                .mpa(new Mpa(1, "G"))
                .build();
        Film result = filmService.update(filmUpdate);

        assertEquals(filmUpdate.getName(), filmService.findFilmById(result.getId()).getName());
    }

    @Test
    public void shouldPassDescriptionValidationWith200Symbols() {
        Film film = Film.builder()
                .name("Аватар")
                .description("«Аватар: Путь воды» (англ. Avatar: The Way of Water) — американский " +
                        "научно-фантастический фильм режиссёра и сценариста Джеймса Кэмерона. Является сиквелом " +
                        "фильма «Аватар» 2009 года.Изначально премьера")
                .duration(120)
                .releaseDate(LocalDate.now())
                .mpa(new Mpa(1, "G"))
                .build();

        Film result = filmService.create(film);
        assertEquals(result.getName(), filmService.findFilmById(result.getId()).getName());
    }

    @Test
    public void shouldPassReleaseDateValidation() {
        Film film = Film.builder()
                .name("rudsruew")
                .description("urdut")
                .duration(120)
                .releaseDate(LocalDate.of(1895, 12, 28))
                .mpa(new Mpa(1, "G"))
                .build();
        filmService.create(film);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());
    }

    @Test
    public void shouldFindFilmById() {
        Film film = filmService.create(newFilm);
        assertEquals(film.getName(), filmService.findFilmById(film.getId()).getName());
    }

    @Test
    public void shouldAddLike() {
        User user1 = userService.create(user);
        Film film1 = filmService.create(newFilm);

        filmService.addLike(film1.getId(), user1.getId());
        assertEquals(1, likeStorage.count(film1.getId()));
    }

    @Test
    public void shouldDeleteLike() {
        User user1 = userService.create(user);
        Film film1 = filmService.create(newFilm);

        filmService.addLike(film1.getId(), user1.getId());
        filmService.deleteLike(film1.getId(), user1.getId());

        assertEquals(0, likeStorage.count(film1.getId()));
    }

    @Test
    public void shouldGetMostPopularFilms() {
        User user1 = userService.create(user);

        User secondUser = User.builder()
                .login("Iris")
                .name("Melissa")
                .email("meow@mail.ru")
                .birthday(LocalDate.of(2000, 8, 15))
                .build();
        User user2 = userService.create(secondUser);

        Film film1 = filmService.create(newFilm);
        filmService.addLike(film1.getId(), user1.getId());
        filmService.addLike(film1.getId(), user2.getId());
        assertEquals(List.of(filmService.findFilmById(film1.getId())), filmService.getMostPopularFilms(1));
    }
}