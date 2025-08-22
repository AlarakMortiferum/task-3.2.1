package ru.netology.ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement codeField = $("[data-test-id=code] input");
    private final SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public DashboardPage validVerify(String code) {
        codeField.setValue(code);
        verifyButton.click();
        return new DashboardPage();
    }

    public void verifyErrorNotification(String expectedText) {
        errorNotification.shouldBe(visible).shouldHave(text(expectedText));
    }

    public void enterWrongCodeMultipleTimes(String wrongCode, int times) {
        for (int i = 0; i < times; i++) {
            codeField.setValue(wrongCode);
            verifyButton.click();
            errorNotification.shouldBe(visible);
        }
    }
}