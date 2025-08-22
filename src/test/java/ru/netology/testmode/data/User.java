package ru.netology.testmode.data;

import lombok.Value;

@Value
public class User {
    String login;
    String password;
    String status;
}