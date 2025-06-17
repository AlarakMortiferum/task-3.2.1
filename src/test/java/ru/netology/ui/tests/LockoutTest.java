package ru.netology.testmode.ui.tests;

import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;
import ru.netology.testmode.data.SQLHelper;
import ru.netology.testmode.ui.pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class LockoutTest {

    @Test
    void shouldBlockUserAfterThreeWrong2FACodes() {
        var user = DataGenerator.getActiveUser();
        open("/login");

        var verificationPage = new LoginPage()
                .loginWith(user.getLogin(), user.getPassword());

        // Вводим трижды неправильный код
        for (int i = 0; i < 3; i++) {
            verificationPage.enterCode("000000") // неверный код
                    .shouldSeeError("Ошибка! Неверно указан код! Попробуйте ещё раз.");
        }

        // После 3-х попыток пользователь должен быть заблокирован — проверим это
        open("/login");
        new LoginPage()
                .loginWith(user.getLogin(), user.getPassword())
                .shouldSeeError("Ошибка! Пользователь заблокирован");
    }
}
