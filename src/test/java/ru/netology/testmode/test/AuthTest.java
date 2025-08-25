package ru.netology.testmode.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import ru.netology.testmode.data.DataHelper;
import ru.netology.testmode.data.SQLHelper;
import ru.netology.testmode.data.User;
import ru.netology.ui.pages.DashboardPage;
import ru.netology.ui.pages.LoginPage;
import ru.netology.ui.pages.VerificationPage;

import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    @BeforeAll
    static void setupAll() {
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

        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(user);
        verificationPage.isPageLoaded(); // Добавьте эту строку
        String code = SQLHelper.getVerificationCodeFor(user);
        DashboardPage dashboardPage = verificationPage.validVerify(code);
        dashboardPage.shouldBeVisible();
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        User user = DataHelper.getRegisteredActiveUser();
        SQLHelper.addUserIfNeeded(user);
        SQLHelper.updateUserStatus(user.getLogin(), "blocked");

        LoginPage loginPage = new LoginPage();
        loginPage.validLogin(user);
        loginPage.verifyNotification("Ошибка! Пользователь заблокирован"); // Обновленный текст
    }

    @Test
    void shouldNotLoginWithWrongLogin() {
        User user = DataHelper.getWrongLoginUser();
        LoginPage loginPage = new LoginPage();
        loginPage.validLogin(user);
        loginPage.verifyNotification("Ошибка! Неверно указан логин или пароль"); // Обновленный текст
    }

    @Test
    void shouldNotLoginWithWrongPassword() {
        User validUser = DataHelper.getRegisteredActiveUser();
        SQLHelper.addUserIfNeeded(validUser);

        LoginPage loginPage = new LoginPage();
        User wrongPasswordUser = new User(validUser.getLogin(), "wrongPassword", validUser.getStatus());
        loginPage.validLogin(wrongPasswordUser);
        loginPage.verifyNotification("Неверно указан логин или пароль");
    }

    @Test
    void shouldBlockUserAfterThreeWrong2FACodes() {
        User user = DataHelper.getRegisteredActiveUser();
        SQLHelper.addUserIfNeeded(user);

        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(user);
        verificationPage.isPageLoaded();
        verificationPage.enterWrongCodeMultipleTimes(DataHelper.getWrongVerificationCode(), 3);
        verificationPage.verifyErrorNotification("Ошибка! Пользователь заблокирован");
    }
}