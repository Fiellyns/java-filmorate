package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping ("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    private int idForUser = 0;

    public int idGeneration() {
        return ++idForUser;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {

        //Если я убираю null, то не проходит тест на Постмане.
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Не найдено имя. Теперь имя = логин");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Введена неверная дата рождения.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        user.setId(idGeneration());
        log.info("Добавлен новый юзер");
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
                log.warn("Произошла замена ID");
                throw new ValidationException("ID не может быть изменён.");
        } else {

            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.warn("Введена неверная дата рождения.");
                throw new ValidationException("Дата рождения не может быть в будущем.");
            }

            if (user.getName() == null || user.getName().isBlank()) {
                log.info("Не найдено имя. Теперь имя = логин");
                user.setName(user.getLogin());
            }
            log.info("Юзер с id: " + user.getId() + " был обновлен.");
            users.put(user.getId(), user);
            return user;
        }
    }
}
