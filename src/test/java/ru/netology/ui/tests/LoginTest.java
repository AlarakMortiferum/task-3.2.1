package ru.netology.ui.tests;

import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;
import ru.netology.ui.pages.LoginPage; // Исправленный импорт

import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    @Test
    void shouldLoginWithValidActiveUser() {
        var user = DataGenerator.getActiveUser();
        open("http://localhost:9999");

        new LoginPage()
                .loginWith(user.getLogin(), user.getPassword())
                .shouldSeeDashboard();
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        var user = DataGenerator.getBlockedUser();
        open("http://localhost:9999");

        new LoginPage()
                .loginWith(user.getLogin(), user.getPassword())
                .shouldShowErrorNotification("Пользователь заблокирован");
    }

    @Test
    void shouldNotLoginWithInvalidUser() {
        var user = DataGenerator.getRandomUser();
        open("http://localhost:9999");

        new LoginPage()
                .loginWith(user.getLogin(), user.getPassword())
                .shouldShowErrorNotification("Неверно указан логин или пароль");
    }
}