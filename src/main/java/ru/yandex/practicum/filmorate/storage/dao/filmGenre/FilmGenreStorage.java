package ru.yandex.practicum.filmorate.storage.dao.filmGenre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreStorage {

    List<Genre> getGenres();

    Genre getGenreById(int genreId);

    void delete(Film film);

    void add(Film film);

    List<Genre> getFilmGenres(int filmId);
}
