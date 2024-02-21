package ru.yandex.practicum.filmorate.storage.dao.friendship;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component("FriendshipDbStorage")
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriend(int userFirst, int userSecond) {
        String sql = "insert into friendships (user_first_id, user_second_id) values (?,?);";
        jdbcTemplate.update(sql, userFirst, userSecond);
        log.info("Пользователи с id {} и {} теперь друзья", userFirst, userSecond);
    }

    public void removeFromFriends(int userFirst, int userSecond) {
        String sql = "delete from friendships where user_first_id = ? and user_second_id = ?;";
        jdbcTemplate.update(sql, userFirst, userSecond);
        log.info("Пользователи с id {} и {} теперь не являются друзья", userFirst, userSecond);
    }

    public Collection<Integer> findFriendsFromUserId(int userId) {
        String sql = "select user_first_id, user_second_id from friendships where user_first_id = ?;";
        List<Integer> friendships = jdbcTemplate.query(sql, new FriendMapper(), userId)
                .stream()
                .map(Friendship::getUserSecond)
                .collect(Collectors.toList());
        log.trace("Найдены друзья пользователя с id_{}: {}", userId, friendships);
        return friendships;
    }

}
