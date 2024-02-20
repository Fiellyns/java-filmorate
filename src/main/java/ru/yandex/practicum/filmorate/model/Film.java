package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.exception.ReleaseDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder(toBuilder = true)
public class Film {

    private int id;
    @NotBlank
    @NotEmpty(message = "Название не должно быть пустым")
    private String name;
    @NotNull
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    @NotNull
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательной")
    @NotNull
    private int duration;
    @NotNull
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                ", mpa=" + mpa +
                ", genres=" + genres +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film = (Film) o;
        return getId() == film.getId() && getDuration() == film.getDuration() && Objects.equals(getName(), film.getName()) && Objects.equals(getDescription(), film.getDescription()) && Objects.equals(getReleaseDate(), film.getReleaseDate()) && Objects.equals(getMpa(), film.getMpa()) && Objects.equals(getGenres(), film.getGenres());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getReleaseDate(), getDuration(), getMpa(), getGenres());
    }
}
