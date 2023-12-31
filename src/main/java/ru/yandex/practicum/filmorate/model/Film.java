package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ReleaseDate;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder (toBuilder = true)
public class Film {
    private int id = 0;
    @NotBlank
    @NotEmpty(message = "Название не должно быть пустым")
    private String name;
    @NotNull
    @Size (max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    @NotNull
    @ReleaseDate
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательной")
    @NotNull
    private int duration;
}
