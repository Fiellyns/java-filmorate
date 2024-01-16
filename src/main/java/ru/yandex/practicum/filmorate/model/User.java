package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class User {
    private final Set<Integer> friends = new HashSet<>();
    private int id;
    @NotNull
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Неверный формат адреса электронной почты")
    private String email;
    @Pattern(regexp = "^[^\\s]+$")
    @NotEmpty(message = "Логин не может быть пустым")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
}
