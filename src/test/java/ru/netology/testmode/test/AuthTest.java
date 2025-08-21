package ru.netology.testmode.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import ru.netology.testmode.data.DataHelper;
import ru.netology.testmode.data.SQLHelper;
import ru.netology.testmode.data.User;
import ru.netology.ui.pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    @BeforeAll
    static void setupAll() {
        Configuration.headless = true;
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @AfterAll
    static void tearDownAll() {
        SQLHelper.clearDatabase();
    }

    @Test
    void shouldLoginWithActiveUser() {
        User user = DataHelper.getRegisteredActiveUser();
        SQLHelper.addUserIfNeeded(user);

        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(user);
        var code = SQLHelper.getVerificationCodeFor(user);
        verificationPage.validVerify(code)
                .shouldSeeDashboard();
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        User user = DataHelper.getRegisteredBlockedUser();
        SQLHelper.addUserIfNeeded(user);

        var loginPage = new LoginPage();
        loginPage.validLogin(user);
        loginPage.verifyUserBlocked();
    }

    @Test
    void shouldNotLoginWithWrongLogin() {
        var loginPage = new LoginPage();
        var user = DataHelper.getWrongLoginUser();
        loginPage.validLogin(user);
        loginPage.verifyErrorNotification();
    }

    @Test
    void shouldNotLoginWithWrongPassword() {
        User validUser = DataHelper.getRegisteredActiveUser();
        SQLHelper.addUserIfNeeded(validUser);

        var loginPage = new LoginPage();
        var wrongPasswordUser = new User(validUser.getLogin(), "wrongPassword", validUser.getStatus());
        loginPage.validLogin(wrongPasswordUser);
        loginPage.verifyErrorNotification();
    }

    @Test
    void shouldLoginSuccessfullyWith2FA() {
        User user = DataHelper.getRegisteredActiveUser();
        SQLHelper.addUserIfNeeded(user);

        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(user);
        var code = SQLHelper.getVerificationCodeFor(user);
        verificationPage.validVerify(code)
                .shouldSeeDashboard();
    }

    @Test
    void shouldBlockUserAfterThreeWrong2FACodes() {
        User user = DataHelper.getRegisteredActiveUser();
        SQLHelper.addUserIfNeeded(user);

        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(user);
        verificationPage.enterWrongCodeThreeTimes();
        verificationPage.shouldShowBlockedNotification();
    }

    @Test
    void shouldVerifyCodeAfterAuth() {
        User user = DataHelper.getRegisteredActiveUser();
        SQLHelper.addUserIfNeeded(user);

        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(user);
        var code = SQLHelper.getVerificationCodeFor(user);
        verificationPage.validVerify(code)
                .shouldSeeDashboard();
    }
}