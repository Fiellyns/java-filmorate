package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int id;
    @NotNull
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Неверный формат адреса электронной почты")
    private String email;
    @Pattern(regexp = "^[^\\s]+$")
    @NotBlank
    @NotEmpty(message = "Логин не может быть пустым")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    public String getName() {
        if (name == null || name.isBlank()) {
            name = login;
        }
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getLogin(), user.getLogin()) && Objects.equals(getName(), user.getName()) && Objects.equals(getBirthday(), user.getBirthday());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getLogin(), getName(), getBirthday());
    }
}
