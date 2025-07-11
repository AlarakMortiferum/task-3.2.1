package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import java.util.Locale;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    public static User getActiveUser() {
        return new User("vasya", "qwerty123", "active");
    }

    public static User getBlockedUser() {
        return new User("petya", "asdfgh456", "blocked");
    }

    public static User getRandomUser() {
        return new User(
                faker.name().username(),
                faker.internet().password(),
                "active"
        );
    }
}