package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
    @NotNull
    private int userFirst;
    @NotNull
    private int userSecond;

    @Override
    public String toString() {
        return "Friendship{" +
                "userFirst=" + userFirst +
                ", userSecond=" + userSecond +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship)) return false;
        Friendship that = (Friendship) o;
        return getUserFirst() == that.getUserFirst() && getUserSecond() == that.getUserSecond();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserFirst(), getUserSecond());
    }
}


