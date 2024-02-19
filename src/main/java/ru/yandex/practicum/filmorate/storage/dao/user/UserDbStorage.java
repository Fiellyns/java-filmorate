package ru.yandex.practicum.filmorate.storage.dao.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Objects;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User create(User user) {
        String sql = "insert into users (email, login, name, birthday) values (?,?,?,?)";
        String sqlForId = "select * from users where email = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        User result = jdbcTemplate.queryForObject(sqlForId, new UserMapper(), user.getEmail());
        user.setId(Objects.requireNonNull(result).getId());
        log.info("Добавлен новый юзер {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "update users set email = ?, login = ?, name = ?, birthday = ? where user_id = ?;";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        log.info("Обновление юзера {}", user);
        return user;
    }

    @Override
    public User findUserById(int id) {
        try {
            String sql = "select * from users where user_id = ?";
            return jdbcTemplate.queryForObject(sql, new UserMapper(), id);
        } catch (Exception e) {
            log.warn("Пользователь с id_{} не найден", id);
            throw new UserNotFoundException();
        }
    }

    @Override
    public Collection<User> getUsers() {
        String sql = "select * from users";
        Collection<User> usersFromDb = jdbcTemplate.query(sql, new UserMapper());
        log.info("Возвращены все пользователи: {}.", usersFromDb);
        return usersFromDb;
    }
}
