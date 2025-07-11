package ru.netology.testmode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataHelper;
import ru.netology.testmode.data.SQLHelper;
import ru.netology.ui.pages.LoginPage; // Исправленный импорт

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.SQLHelper.clearDatabase;

public class AuthTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @BeforeEach
    void clearAll() throws Exception {
        clearDatabase();
    }

    @Test
    void shouldLoginWithActive() {
        var loginPage = new LoginPage();
        var user = DataHelper.getRegisteredActiveUser();
        var verificationPage = loginPage.validLogin(user);
        verificationPage.validVerify();
    }

    @Test
    void shouldNotLoginWithBlocked() {
        var loginPage = new LoginPage();
        var user = DataHelper.getRegisteredBlockedUser();
        loginPage.validLogin(user);
        loginPage.verifyUserBlocked();
    }

    @Test
    void shouldNotLoginWrongLogin() {
        var loginPage = new LoginPage();
        var user = DataHelper.getWrongLoginUser();
        loginPage.validLogin(user);
        loginPage.verifyErrorNotification();
    }

    @Test
    void shouldNotLoginWrongPassword() {
        var loginPage = new LoginPage();
        var user = DataHelper.getWrongPasswordUser();
        loginPage.validLogin(user);
        loginPage.verifyErrorNotification();
    }
}