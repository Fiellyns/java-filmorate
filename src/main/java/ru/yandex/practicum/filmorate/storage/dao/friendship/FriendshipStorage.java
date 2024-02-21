package ru.yandex.practicum.filmorate.storage.dao.friendship;

import java.util.Collection;

public interface FriendshipStorage {

    void addFriend(int userFirst, int userSecond);

    void removeFromFriends(int userFirst, int userSecond);

    Collection<Integer> findFriendsFromUserId(int userId);
}
