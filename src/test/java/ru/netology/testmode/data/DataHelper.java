package ru.netology.testmode.data;

public class DataHelper {
    public static User getRegisteredActiveUser() {
        return new User("vasya", "qwerty123", "active");
    }

    public static User getRegisteredBlockedUser() {
        return new User("petya", "123qwerty", "blocked");
    }

    public static User getWrongLoginUser() {
        return new User("wrong_login", "qwerty123", "active");
    }

    public static User getWrongPasswordUser() {
        return new User("vasya", "wrong_password", "active");
    }

    public static String getWrongVerificationCode() {
        return "000000";
    }
}