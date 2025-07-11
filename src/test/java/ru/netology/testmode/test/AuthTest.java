package ru.netology.testmode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataHelper;
import ru.netology.testmode.data.SQLHelper;
import ru.netology.ui.pages.LoginPage;
import ru.netology.ui.pages.VerificationPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.SQLHelper.clearDatabase;

public class AuthTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @BeforeEach
    void clearAll() throws SQLException {
        clearDatabase();
    }

    @Test
    void shouldLoginWithActive() throws SQLException {
        var loginPage = new LoginPage();
        var user = DataHelper.getRegisteredActiveUser();
        var verificationPage = loginPage.validLogin(user);
        var code = SQLHelper.getVerificationCodeFor(user);
        verificationPage.validVerify(code)
                .shouldSeeDashboard();
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