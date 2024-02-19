package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.dao.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage,
                       @Qualifier("FriendshipDbStorage") FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        findUserById(user.getId());
        return userStorage.update(user);
    }

    public User findUserById(int id) {
        return userStorage.findUserById(id);
    }

    public void addFriend(int userId, int friendId) {
        findUserById(friendId);
        friendshipStorage.addFriend(userId, friendId);
    }

    public void removeFromFriends(int userId, int friendId) {
        findUserById(friendId);
        friendshipStorage.removeFromFriends(userId, friendId);
    }

    public List<User> getAllFriends(int userId) {
        findUserById(userId);
        List<User> friends = friendshipStorage.findFriendsFromUserId(userId).stream()
                .mapToInt(Integer::valueOf)
                .mapToObj(userStorage::findUserById)
                .collect(Collectors.toList());
        log.trace("Возвращён список друзей: {}", friends);
        return friends;
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        List<User> commonFriends = CollectionUtils.intersection(
                        friendshipStorage.findFriendsFromUserId(userId),
                        friendshipStorage.findFriendsFromUserId(otherUserId)).stream()
                .mapToInt(Integer::valueOf)
                .mapToObj(userStorage::findUserById)
                .collect(Collectors.toList());
        log.trace("Возвращён список общих друзей: {}", commonFriends);
        return commonFriends;
    }

}