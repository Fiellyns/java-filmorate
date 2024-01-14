package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private int idNext = 1;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    private int getNextId() {
        return idNext++;
    }

    public List<Film> getFilms() {
        return List.copyOf(filmStorage.getAllFilms());
    }

    public Film create(Film film) {
        film.setId(getNextId());
        log.info("Добавлен новый фильм");
        return filmStorage.createFilm(film);
    }

    public Film update(Film film) {
        if (filmStorage.findFilmById(film.getId()) == null) {
            log.warn("Невозможно обновить фильм");
            throw new FilmDoesNotExistException();
        } else {
            if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
                log.warn("Введена неверная дата фильма");
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
            }
            log.info("Фильм с id: {} был обновлен", film.getId());
            return filmStorage.update(film);
        }
    }

    public Film findFilmById(int id) {
        Film film = filmStorage.findFilmById(id);
        if (film == null) {
            throw new FilmDoesNotExistException();
        }
        return film;
    }

    public void addLike(int filmId, int userId) {
        if (userService.findUserById(userId) != null) {
            findFilmById(filmId).getLikes().add(userId);
            log.info("Пользователь с userId {} поставил фильму с filmId {} лайк", userId, filmId);
        }
    }

    public void deleteLike(int filmId, int userId) {
        if (userService.findUserById(userId) != null) {
            findFilmById(filmId).getLikes().remove(userId);
            log.info("Лайк пользователя с userId {} фильму с filmId {} удалён", userId, filmId);
        }
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmStorage.getAllFilms()
                .stream()
                .sorted((f1, f2) ->
                        f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

}
