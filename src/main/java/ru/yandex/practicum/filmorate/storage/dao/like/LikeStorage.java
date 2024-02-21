package ru.yandex.practicum.filmorate.storage.dao.like;

public interface LikeStorage {
    void add(int filmID, int userID);

    void delete(int filmID, int userID);

    int count(int filmID);
}
