package ru.yandex.practicum.filmorate.service;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return List.copyOf(userStorage.getUsers());
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User findUserById(int id) {
        return userStorage.findUserById(id);
    }

    public void addFriend(int userId, int friendId) {
        User user = findUserById(userId);
        User friend = findUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("Пользователи {} и {} теперь друзья", user, friend);
    }

    public void removeFromFriends(int userId, int friendId) {
        User user = findUserById(userId);
        User friend = findUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("Пользователи {} и {} теперь не являются друзья", user, friend);
    }

    public List<User> getAllFriends(int userId) {
        List<User> friends = new ArrayList<>();
        User user = findUserById(userId);
        for (int id : user.getFriends()) {
            friends.add(findUserById(id));
        }
        return friends;
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        List<User> commonFriends = new ArrayList<>();
        User user = findUserById(userId);
        User otherUser = findUserById(otherUserId);
        Set<Integer> commonFriendsIds = Sets.intersection(user.getFriends(), otherUser.getFriends());
        for (int id : commonFriendsIds) {
            commonFriends.add(findUserById(id));
        }
        return commonFriends;
    }
}
