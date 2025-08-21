package ru.netology.ui.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.testmode.data.User;
import ru.netology.ui.pages.VerificationPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginField = $("[data-test-id=login] input");
    private final SelenideElement passwordField = $("[data-test-id=password] input");
    private final SelenideElement loginButton = $("[data-test-id=action-login]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public VerificationPage validLogin(User user) {
        loginField.setValue(user.getLogin());
        passwordField.setValue(user.getPassword());
        loginButton.click();
        return new VerificationPage();
    }

    public VerificationPage loginWith(String login, String password) {
        loginField.setValue(login);
        passwordField.setValue(password);
        loginButton.click();
        return new VerificationPage();
    }

    public void verifyUserBlocked() {
        errorNotification.shouldBe(visible)
                .shouldHave(text("Пользователь заблокирован"));
    }

    public void verifyErrorNotification() {
        errorNotification.shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }
}