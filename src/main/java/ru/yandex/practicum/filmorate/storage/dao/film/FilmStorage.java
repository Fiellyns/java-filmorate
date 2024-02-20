package ru.yandex.practicum.filmorate.storage.dao.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms();

    Film createFilm(Film film);

    Film update(Film film);

    Film findFilmById(int id);

    List<Film> getMostPopularFilms(int count);
}
