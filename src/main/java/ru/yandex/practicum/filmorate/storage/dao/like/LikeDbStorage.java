package ru.yandex.practicum.filmorate.storage.dao.like;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component("LikeDbStorage")
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(int filmID, int userID) {
        String sql = "insert into likes (film_id, user_id) values (?,?)";
        jdbcTemplate.update(sql, filmID, userID);
        log.info("Фильму c id_{} добавлен лайк от пользователя с id_{}.", filmID, userID);
    }

    @Override
    public void delete(int filmID, int userID) {
        String sql = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sql, filmID, userID);
        log.info("У фильма с id_{} удалён лайк от пользователя с id_{}.", filmID, userID);
    }

    @Override
    public int count(int filmID) {
        String sql = "select count(film_id) from likes where film_id = ?";
        Integer count = Objects.requireNonNull(jdbcTemplate.queryForObject(sql, Integer.class, filmID));
        log.trace("Подсчитано количество лайков для фильма id_{}: {}.", filmID, count);
        return count;
    }
}
