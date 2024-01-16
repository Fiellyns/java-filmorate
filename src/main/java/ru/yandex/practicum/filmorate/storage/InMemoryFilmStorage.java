package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int idNext = 1;

    private int getNextId() {
        return idNext++;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(getNextId());
        log.info("Добавлен новый фильм c id: {}", film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Невозможно обновить фильм");
            throw new FilmDoesNotExistException();
        } else {
            if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
                log.warn("Введена неверная дата фильма");
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
            }
            log.info("Фильм с id: {} был обновлен", film.getId());
            films.put(film.getId(), film);
            return film;
        }
    }

    @Override
    public Film findFilmById(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        log.warn("Невозможно обновить фильм");
        throw new FilmDoesNotExistException();
    }
}
