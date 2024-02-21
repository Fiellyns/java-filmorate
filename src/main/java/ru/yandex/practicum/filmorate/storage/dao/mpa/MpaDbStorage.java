package ru.yandex.practicum.filmorate.storage.dao.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Component("MpaDbStorage")
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(int mpaId) {
        try {
            String sql = "select * from ratings_mpa where rating_id = ?";
            return jdbcTemplate.queryForObject(sql, new MpaMapper(), mpaId);
        } catch (Exception e) {
            log.warn("Рейтинг с id_" + mpaId + " не найден!");
            throw new MpaNotFoundException("id_" + mpaId);
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "select * from ratings_mpa";
        return jdbcTemplate.query(sql, new MpaMapper());
    }
}
