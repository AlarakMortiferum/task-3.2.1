package ru.netology.ui.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.testmode.data.User;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement codeField = $("[data-test-id=code] input");
    private final SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");
    private final SelenideElement dashboard = $("[data-test-id=dashboard]");

    public VerificationPage validVerify(String code) {
        codeField.setValue(code);
        verifyButton.click();
        return this;
    }

    public void shouldSeeDashboard() {
        dashboard.shouldBe(visible);
    }

    public void enterWrongCodeThreeTimes() {
        // Реализация трёхкратного ввода неправильного кода
    }

    public void shouldShowBlockedNotification() {
        errorNotification.shouldBe(visible)
                .shouldHave(text("Пользователь заблокирован"));
    }
}