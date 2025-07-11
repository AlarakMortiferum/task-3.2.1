package ru.netology.ui.tests;

import ru.netology.testmode.data.SQLHelper;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;
import ru.netology.ui.pages.LoginPage;
import ru.netology.ui.pages.VerificationPage;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    @Test
    void shouldLoginWithValidActiveUser() throws SQLException {
        var user = DataGenerator.getActiveUser();
        open("http://localhost:9999");

        var verificationPage = new LoginPage()
                .loginWith(user.getLogin(), user.getPassword()); // Исправлено


        var code = SQLHelper.getVerificationCodeFor(user);
        verificationPage.validVerify(code)
                .shouldSeeDashboard();
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        var user = DataGenerator.getBlockedUser();
        open("http://localhost:9999");

        new LoginPage()
                .loginWith(user.getLogin(), user.getPassword())
                .shouldShowBlockedNotification();
    }

    @Test
    void shouldNotLoginWithInvalidUser() {
        var user = DataGenerator.getRandomUser();
        open("http://localhost:9999");

        var loginPage = new LoginPage();
        loginPage.loginWith(user.getLogin(), user.getPassword());
        loginPage.verifyErrorNotification();
    }
}