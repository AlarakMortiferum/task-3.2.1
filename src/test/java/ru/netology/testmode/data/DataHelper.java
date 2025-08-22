package ru.netology.testmode.data;

public class DataHelper {
    public static User getRegisteredActiveUser() {
        return new User("vasya", "qwerty123", "active");
    }

    public static User getRegisteredBlockedUser() {
        return new User("petya", "asdfgh456", "blocked");
    }

    public static User getWrongLoginUser() {
        return new User("wrongLogin", "qwerty123", "active");
    }

    public static User getWrongPasswordUser() {
        return new User("vasya", "wrongPassword", "active");
    }

    public static String getWrongVerificationCode() {
        return "000000";
    }
}