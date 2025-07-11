package ru.netology.ui.tests;

import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;
import ru.netology.testmode.data.SQLHelper;
import ru.netology.ui.pages.LoginPage;
import ru.netology.testmode.data.User;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;

public class Login2FATest {

    @Test
    void shouldLoginSuccessfullyWithCorrectCode() throws SQLException {
        var user = DataGenerator.getActiveUser();
        open("http://localhost:9999");

        var verificationPage = new LoginPage()
                .loginWith(user.getLogin(), user.getPassword()); // Исправлено

        var code = SQLHelper.getVerificationCodeFor(user);
        verificationPage.validVerify(code)
                .shouldSeeDashboard();
    }
}