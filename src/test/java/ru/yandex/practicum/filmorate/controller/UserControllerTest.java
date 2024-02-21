package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.dao.friendship.FriendshipStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    private final UserService userService;
    private final FriendshipStorage friendshipStorage;
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();
    private final User user = User.builder()
            .login("a")
            .name("nisi eiusmod")
            .email("gh1u@mail.ru")
            .birthday(LocalDate.now())
            .build();


    @Test
    public void shouldCreateUser() {
        User newUser = userService.create(user);
        assertEquals("gh1u@mail.ru", userService.findUserById(newUser.getId()).getEmail());
    }

    @Test
    public void shouldUpdateUser() {
        User newUser = userService.create(user);

        User userUpdate = User.builder()
                .id(newUser.getId())
                .login("a")
                .name("nisi eiusmod")
                .email("gh1u@mail.ru")
                .birthday(LocalDate.now())
                .build();
        User result = userService.update(userUpdate);

        assertEquals(userUpdate, userService.findUserById(result.getId()));
    }

    @Test
    public void shouldCreateUserWithEmptyName() {
        User newUser = User.builder()
                .login("ab")
                .email("gh1u@mail.ru")
                .birthday(LocalDate.now())
                .build();

        User result = userService.create(newUser);

        assertEquals("ab", result.getName());
    }

    @Test
    void shouldNotPassEmailValidation() {
        User user = User.builder()
                .login("a")
                .name("nisi eiusmod")
                .email("gh1umail.ru")
                .birthday(LocalDate.now())
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    public void shouldNotPassLoginValidationWithEmptyLogin() {
        User newUser = User.builder()
                .login("")
                .name("nisi eiusmod")
                .email("gh1u@mail.ru")
                .birthday(LocalDate.now())
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(newUser);
        assertEquals(3, violations.size());
    }

    @Test
    public void shouldNotPassBirthdayValidation() {
        User user = User.builder()
                .login("a")
                .name("nisi eiusmod")
                .email("gh1u@mail.ru")
                .birthday(LocalDate.of(3000, 8, 15))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    public void shouldAddFriend() {
        User newUser = userService.create(user);
        User friend = User.builder()
                .login("ab")
                .name("atwatwa")
                .email("twatwtwe@mail.ru")
                .birthday(LocalDate.now())
                .build();
        User friendUser = userService.create(friend);

        userService.addFriend(newUser.getId(), friendUser.getId());

        assertEquals(List.of(friendUser), userService.getAllFriends(newUser.getId()));
    }

    @Test
    public void shouldDeleteFriend() {
        User newUser = userService.create(user);
        User friend = User.builder()
                .login("ab")
                .name("atwatwa")
                .email("twatwtwe@mail.ru")
                .birthday(LocalDate.now())
                .build();
        User friendUser = userService.create(friend);

        userService.addFriend(newUser.getId(), friendUser.getId());
        userService.removeFromFriends(newUser.getId(), friendUser.getId());

        assertEquals(Collections.emptyList(), List.copyOf(friendshipStorage.findFriendsFromUserId(newUser.getId())));
    }

    @Test
    public void shouldFindMutualFriend() {
        User newUser = userService.create(user);
        User friend = User.builder()
                .login("ab")
                .name("atwatwa")
                .email("twatwtwe@mail.ru")
                .birthday(LocalDate.now())
                .build();
        User friendUser = userService.create(friend);
        User common = User.builder()
                .login("ab")
                .name("wyawye dtfe")
                .email("twatxe@mail.ru")
                .birthday(LocalDate.of(2000, 8, 15))
                .build();
        User commonFriend = userService.create(common);

        userService.addFriend(newUser.getId(), commonFriend.getId());
        userService.addFriend(friendUser.getId(), commonFriend.getId());

        List<User> commons = userService.getCommonFriends(newUser.getId(), friendUser.getId());

        assertEquals(List.of(commonFriend), commons);
    }

    @Test
    public void shouldReturnAllFriends() {
        User newUser = userService.create(user);
        User friend = User.builder()
                .login("Iris")
                .name("Melissa")
                .email("stormy@mail.ru")
                .birthday(LocalDate.of(2000, 8, 15))
                .build();
        User friendUser = userService.create(friend);

        User common = User.builder()
                .login("Rose")
                .name("Melissa")
                .email("rose@mail.ru")
                .birthday(LocalDate.of(2000, 8, 15))
                .build();
        User commonFriend = userService.create(common);
        userService.addFriend(newUser.getId(), friendUser.getId());
        userService.addFriend(newUser.getId(), commonFriend.getId());

        assertEquals(List.of(friendUser, commonFriend), userService.getAllFriends(newUser.getId()));
    }
}
