package ru.yandex.practicum.filmorate.storage.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreService genreService;
    private final MpaService mpaService;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreService genreService, MpaService mpaService) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreService = genreService;
        this.mpaService = mpaService;
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "insert into films (name, description, release_DATE, duration, rating_id)" +
                "values (?,?,?,?,?)";
        String sqlForId = "select * from films " +
                "where name = ? and description = ? and release_DATE = ? " +
                "and duration = ? and rating_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        Film result = jdbcTemplate.queryForObject(sqlForId, this::mapRowToFilm, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        film.setId(Objects.requireNonNull(result).getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genre.setName(genreService.getGenreById(genre.getId()).getName());
            }
            genreService.putGenres(film);
        }
        log.info("Добавлен новый фильм {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "update films set name=?, description=?," +
                "release_DATE=?, duration=?, rating_id=? where film_id=?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        if (film.getGenres() != null) {
            Collection<Genre> sortGenres = film.getGenres().stream()
                    .sorted(Comparator.comparing(Genre::getId))
                    .collect(Collectors.toList());
            film.setGenres(new LinkedHashSet<>(sortGenres));
            for (Genre genre : film.getGenres()) {
                genre.setName(genreService.getGenreById(genre.getId()).getName());
            }
        }
        genreService.putGenres(film);
        log.info("Обновление фильма {}", film);
        return film;
    }

    @Override
    public Film findFilmById(int id) {
        try {
            String sql = "select * from films as f join ratings_mpa as r on r.rating_id = f.rating_id where film_id = ?;";
            return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
        } catch (Exception e) {
            log.warn("Фильм с id_{} не найден", id);
            throw new FilmNotFoundException("id_" + id);
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "select * from films";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        String getPopularQuery = "select * from films as f left join likes " +
                "as l on l.film_id = f.film_id group by f.film_id, l.user_id " +
                "order by count(l.user_id) desc limit ?";
        return jdbcTemplate.query(getPopularQuery, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_DATE").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpaService.getMpaById(rs.getInt("rating_id")))
                .genres(genreService.getFilmGenres(rs.getInt("film_id")))
                .build();
    }
}
