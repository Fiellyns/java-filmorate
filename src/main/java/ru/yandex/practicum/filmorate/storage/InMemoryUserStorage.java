package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int idNext = 1;

    private int getNextId() {
        return idNext++;
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Введена неверная дата рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        user.setId(getNextId());
        log.info("Добавлен новый юзер c id: {}", user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Невозможно обновить пользователя");
            throw new UserDoesNotExistException();
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Введена неверная дата рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("Юзер с id: {} был обновлен", user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findUserById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        log.warn("Невозможно обновить пользователя");
        throw new UserDoesNotExistException();
    }
}
