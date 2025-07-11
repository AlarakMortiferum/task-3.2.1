package ru.netology.ui.tests;

import org.junit.jupiter.api.*;
import ru.netology.testmode.data.DataHelper;
import ru.netology.ui.pages.LoginPage;
import ru.netology.ui.pages.VerificationPage;

import static com.codeborne.selenide.Selenide.open;

public class LockoutTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should block user after three wrong 2FA codes")
    void shouldBlockUserAfterThreeWrong2FACodes() {
        var loginPage = new LoginPage();
        var user = DataHelper.getRegisteredActiveUser();
        var verificationPage = loginPage.validLogin(user);
        verificationPage.enterWrongCodeThreeTimes();
        verificationPage.shouldShowBlockedNotification();
    }
}