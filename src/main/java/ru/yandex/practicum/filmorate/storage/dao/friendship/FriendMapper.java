package ru.yandex.practicum.filmorate.storage.dao.friendship;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendMapper implements RowMapper<Friendship> {

    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Friendship()
                .toBuilder()
                .userFirst(rs.getInt("user_first_id"))
                .userSecond(rs.getInt("user_second_id"))
                .build();
    }
}
