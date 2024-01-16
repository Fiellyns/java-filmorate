package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getFilms() {
        return List.copyOf(filmStorage.getAllFilms());
    }

    public Film create(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film findFilmById(int id) {
        return filmStorage.findFilmById(id);
    }

    public void addLike(int filmId, int userId) {
        User user = userStorage.findUserById(userId); // Проверка, на существование юзера
        findFilmById(filmId).getLikes().add(userId);
        log.info("Пользователь с userId {} поставил фильму с filmId {} лайк", userId, filmId);
    }

    public void deleteLike(int filmId, int userId) {
        User user = userStorage.findUserById(userId);
        findFilmById(filmId).getLikes().remove(userId);
        log.info("Лайк пользователя с userId {} фильму с filmId {} удалён", userId, filmId);
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
