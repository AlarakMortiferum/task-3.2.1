package ru.netology.testmode.ui.tests;

import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;
import ru.netology.testmode.data.SQLHelper;
import ru.netology.testmode.ui.pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class Login2FATest {

    @Test
    void shouldLoginSuccessfullyWithCorrectCode() {
        var user = DataGenerator.getActiveUser();
        open("/login");

        var verificationPage = new LoginPage()
                .loginWith(user.getLogin(), user.getPassword());

        var code = SQLHelper.getVerificationCodeFor(user);
        verificationPage.enterCode(code)
                .shouldSeeDashboard();
    }
}
