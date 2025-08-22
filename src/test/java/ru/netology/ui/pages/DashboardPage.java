package ru.netology.ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private final SelenideElement dashboard = $("[data-test-id=dashboard]");

    public void shouldBeVisible() {
        dashboard.shouldBe(visible);
    }
}