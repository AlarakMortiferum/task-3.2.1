package ru.netology.testmode.data;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLHelper {
    private static final String url = "jdbc:mysql://localhost:3306/app";
    private static final String user = "app";
    private static final String password = "pass";

    private SQLHelper() {}

    @SneakyThrows
    private static Connection getConnection() {
        return DriverManager.getConnection(url, user, password);
    }

    @SneakyThrows
    public static String getVerificationCodeFor(User dataUser) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT code FROM auth_codes WHERE user_id = " +
                             "(SELECT id FROM users WHERE login = ?) " +
                             "ORDER BY created DESC LIMIT 1")) {
            stmt.setString(1, dataUser.getLogin());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("code");
                }
                throw new SQLException("Verification code not found for user: " + dataUser.getLogin());
            }
        }
    }

    @SneakyThrows
    public static void clearDatabase() {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SET FOREIGN_KEY_CHECKS = 0;" +
                             "TRUNCATE TABLE auth_codes;" +
                             "TRUNCATE TABLE card_transactions;" +
                             "TRUNCATE TABLE cards;" +
                             "TRUNCATE TABLE users;" +
                             "SET FOREIGN_KEY_CHECKS = 1;")) {
            stmt.executeUpdate();
        }
    }
}