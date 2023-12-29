package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder (toBuilder = true)
public class User {
    private int id = 0;
    @NotNull
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",message = "Неверный формат адреса электронной почты")
    private String email;
    @Pattern(regexp = "^[^\\s]+$")
    @NotEmpty(message = "Логин не может быть пустым")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
}
