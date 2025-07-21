package ru.netology.ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
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

    public VerificationPage shouldSeeDashboard() {
        dashboard.shouldBe(visible);
        return this;
    }

    public void enterWrongCodeThreeTimes() {
        for (int i = 0; i < 3; i++) {
            codeField.setValue("000000");
            verifyButton.click();
            errorNotification.shouldBe(visible);
        }
    }

    public void shouldShowBlockedNotification() {
        errorNotification.shouldBe(visible)
                .shouldHave(text("Пользователь заблокирован"));
    }
}