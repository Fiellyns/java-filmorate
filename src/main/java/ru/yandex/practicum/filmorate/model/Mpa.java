package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Mpa {
    private int id;
    private String name;

    @Override
    public String toString() {
        return "Mpa{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mpa)) return false;
        Mpa mpa = (Mpa) o;
        return getId() == mpa.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
