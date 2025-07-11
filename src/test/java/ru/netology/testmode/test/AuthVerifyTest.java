package ru.netology.testmode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataHelper;
import ru.netology.testmode.data.SQLHelper;
import ru.netology.ui.pages.VerificationPage;

import static com.codeborne.selenide.Selenide.open;

public class AuthVerifyTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldVerifyCodeAfterAuth() throws Exception {
        var user = DataHelper.getRegisteredActiveUser();
        var verificationPage = new VerificationPage();
        var code = SQLHelper.getVerificationCodeFor(user);
        verificationPage.validVerify(code);
    }
}