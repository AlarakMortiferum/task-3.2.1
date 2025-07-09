package ru.netology.testmode.ui.pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeInput = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("button[type=button]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public VerificationPage enterCode(String code) {
        codeInput.setValue(code);
        verifyButton.click();
        return this;
    }

    public VerificationPage shouldSeeError(String text) {
        errorNotification.shouldBe(visible).shouldHave(com.codeborne.selenide.Condition.text(text));
        return this;
    }

    public DashboardPage shouldSeeDashboard() {
        // Проверяем, что открылась страница дашборда
        $("[data-test-id=dashboard]").shouldBe(visible);
        return new DashboardPage();
    }
}
