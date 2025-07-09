package ru.netology.testmode.ui.tests;

import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;
import ru.netology.testmode.data.SQLHelper;
import ru.netology.testmode.ui.pages.LoginPage;
import ru.netology.testmode.data.User;

import java.sql.SQLException; // Добавьте этот импорт

import static com.codeborne.selenide.Selenide.open;

public class Login2FATest {

    @Test
    void shouldLoginSuccessfullyWithCorrectCode() throws SQLException { // Добавьте throws SQLException здесь
        var user = DataGenerator.getActiveUser();
        open("/login");

        var verificationPage = new LoginPage()
                .loginWith(user.getLogin(), user.getPassword());

        var code = SQLHelper.getVerificationCodeFor(user); // Теперь исключение обработано
        verificationPage.enterCode(code)
                .shouldSeeDashboard();
    }
}