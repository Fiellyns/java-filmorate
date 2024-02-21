package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.filmGenre.FilmGenreStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final FilmGenreStorage filmGenreStorage;

    @Autowired
    public GenreService(@Qualifier("FilmGenreDbStorage") FilmGenreStorage filmGenreStorage) {
        this.filmGenreStorage = filmGenreStorage;
    }

    public Collection<Genre> getGenres() {
        return filmGenreStorage.getGenres().stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toList());
    }

    public Genre getGenreById(Integer id) {
        return filmGenreStorage.getGenreById(id);
    }

    public void putGenres(Film film) {
        filmGenreStorage.delete(film);
        filmGenreStorage.add(film);
    }

    public Set<Genre> getFilmGenres(int filmId) {
        return new HashSet<>(filmGenreStorage.getFilmGenres(filmId));
    }

}
