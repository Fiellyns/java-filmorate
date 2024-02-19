package ru.yandex.practicum.filmorate.storage.dao.filmGenre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;


@Slf4j
@Component("FilmGenreDbStorage")
public class FilmGenreDbStorage implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void add(Film film) {
        String queryForFilmGenre = "insert into film_genres (film_id, genre_id) VALUES (?, ?)";
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(queryForFilmGenre, film.getId(), genre.getId());
            }
        }
    }

    @Override
    public void delete(Film film) {
        jdbcTemplate.update("delete from film_genres where film_id = ?", film.getId());
    }

    @Override
    public Genre getGenreById(int genreId) {
        try {
            String sql = "select * from genres where genre_id = ?";
            return jdbcTemplate.queryForObject(sql, new GenreMapper(), genreId);
        } catch (Exception e) {
            log.warn("Жанр с id_" + genreId + " не найден!");
            throw new GenreNotFoundException();
        }
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "select * from genres";
        return jdbcTemplate.query(sql, new GenreMapper());
    }

    @Override
    public List<Genre> getFilmGenres(int filmId) {
        String sql = "select fg.genre_id, g.genre_name from film_genres fg join genres g on g.genre_id = fg.genre_id where film_id = ?";
        return jdbcTemplate.query(sql, new GenreMapper(), filmId);
    }

}

