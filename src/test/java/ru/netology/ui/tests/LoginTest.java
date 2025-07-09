package ru.netology.testmode.ui.tests;

import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;
import ru.netology.testmode.ui.pages.LoginPage;
import ru.netology.testmode.data.User;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    @Test
    void shouldLoginWithValidActiveUser() {
        var user = DataGenerator.getActiveUser();
        open("/login");
        new LoginPage()
                .loginWith(user.getLogin(), user.getPassword());
        // переходит на VerificationPage — проверяется в Login2FATest
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        var user = DataGenerator.getBlockedUser();
        open("/login");
        new LoginPage()
                .loginWith(user.getLogin(), user.getPassword())
                .shouldSeeError("Ошибка! Пользователь заблокирован");
    }

    @Test
    void shouldNotLoginWithInvalidUser() {
        var user = DataGenerator.getRandomUser(); // пользователь не зарегистрирован
        open("/login");
        new LoginPage()
                .loginWith(user.getLogin(), user.getPassword())
                .shouldSeeError("Ошибка! Неверно указан логин или пароль");
    }
}
