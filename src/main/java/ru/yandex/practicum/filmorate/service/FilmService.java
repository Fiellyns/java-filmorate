package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.dao.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage,
                       @Qualifier("LikeDbStorage") LikeDbStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    public Film create(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film update(Film film) {
        findFilmById(film.getId());
        return filmStorage.update(film);
    }

    public Film findFilmById(int id) {
        return filmStorage.findFilmById(id);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    public Collection<Film> getMostPopularFilms(int count) {
        return filmStorage.getMostPopularFilms(count);
    }


    public void addLike(int filmId, int userId) {
        userStorage.findUserById(userId);
        filmStorage.findFilmById(filmId);
        likeStorage.add(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        userStorage.findUserById(userId);
        filmStorage.findFilmById(filmId);
        likeStorage.delete(filmId, userId);
    }
}
