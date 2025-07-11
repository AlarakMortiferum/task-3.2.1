package ru.netology.testmode.ui.pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {
    private SelenideElement login = $(byName("login"));
    private SelenideElement password = $(byName("password"));
    private SelenideElement submit = $("button[type='submit']");

    public LoginPage openPage() {
        open("/login");
        return this;
    }

    public VerificationPage loginWith(String lg, String pw) {
        login.setValue(lg);
        password.setValue(pw);
        submit.click();
        return new VerificationPage();
    }

    public LoginPage shouldSeeError(String text) {
        $("[data-test-id=error-notification]")
                .shouldBe(visible).shouldHave(com.codeborne.selenide.Condition.text(text));
        return this;
    }
    public VerificationPage validLogin(User user) {
        loginField.setValue(user.getLogin());
        passwordField.setValue(user.getPassword());
        loginButton.click();
        return new VerificationPage();
    }

    public void verifyUserBlocked() {
        errorNotification.shouldBe(visible)
                .shouldHave(text("Пользователь заблокирован"));
    }
}