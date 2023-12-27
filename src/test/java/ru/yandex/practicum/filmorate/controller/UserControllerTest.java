package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class UserControllerTest {
    private final UserController userController = new UserController();
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    private final User user = User.builder()
            .login("a")
            .name("nisi eiusmod")
            .email("gh1u@mail.ru")
            .birthday(LocalDate.now())
            .build();

    @Test
    void shouldCreateUser() {
        userController.createUser(user);
        assertEquals(userController.getAllUsers().size(), 1);
    }

    @Test
    void shouldNotCreateUserIfLoginIsWrong() {
        String[] logins = {"dolore ullamco", "d olore ullamc o", "", " ", null};

        Arrays.stream(logins).forEach(login -> {
            User userWithIncorrectLogin = user
                    .toBuilder()
                    .login(login)
                    .build();

             Set<ConstraintViolation<User>> violations = validator.validate(userWithIncorrectLogin);

            Assertions.assertFalse(violations.isEmpty());
        });
    }

    @Test
    void shouldNotCreateUserIfEmailIsWrong() {
        String[] emails = {"user @domain.com", ".user@domain.co.in", "@domain.com", "user?name@doma in.co.in",
                "@domain.com",
                "",
                null};

        Arrays.stream(emails).forEach(email -> {
            User userWithIncorrectEmail = user
                    .toBuilder()
                    .email(email)
                    .build();

            Set<ConstraintViolation<User>> violations = validator.validate(userWithIncorrectEmail);

            Assertions.assertFalse(violations.isEmpty());
        });
    }

    @Test
    void shouldNotCreateUserIfBirthdayIsWrong() {
        User userWithIncorrectBirthday = user
                .toBuilder()
                .birthday(LocalDate.now().plusDays(1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(userWithIncorrectBirthday);

        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals(1, violations.size());
    }
}
