package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.debug("Поступил GET-запрос в /users.");
        return userService.getUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.debug("Поступил POST-запрос в /users");
        return userService.create(user);
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User user) {
        log.debug("Поступил PUT-запрос в /users");
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable int id) {
        log.debug("Поступил GET-запрос в /users/{}", id);
        return userService.findUserById(id);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Поступил PUT-запрос в /users/{}/friends/{}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Поступил DELETE-запрос в /users/{}/friends/{}", id, friendId);
        userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable int id) {
        log.debug("Поступил GET-запрос в /users/{}/friends", id);
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.debug("Поступил GET-запрос в /users/{}/friends/common/{}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
