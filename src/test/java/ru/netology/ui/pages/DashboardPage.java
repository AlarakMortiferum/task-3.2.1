package ru.netology.testmode.ui.pages;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.visible;

public class DashboardPage {
    public DashboardPage shouldBeVisible() {
        $("[data-test-id=dashboard]").shouldBe(visible);
        return this;
    }
}
