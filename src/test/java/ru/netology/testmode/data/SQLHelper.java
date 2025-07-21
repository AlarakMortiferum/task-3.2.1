package ru.netology.testmode.data;

import java.sql.*;

public class SQLHelper {
    private static final String url = System.getProperty("jdbc.url", "jdbc:postgresql://localhost:5432/app");
    private static final String dbUser = System.getProperty("jdbc.user", "app"); // Переименовано
    private static final String dbPassword = System.getProperty("jdbc.password", "pass"); // Переименовано

    private SQLHelper() {}

    public static String getVerificationCodeFor(User dataUser) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
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

    public static void clearDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM auth_codes");
            stmt.executeUpdate("DELETE FROM card_transactions");
            stmt.executeUpdate("DELETE FROM cards");
            stmt.executeUpdate("DELETE FROM users");
        }
    }

    public static void addUserIfNeeded(User newUser) throws SQLException { // Переименован параметр
        if (!userExists(newUser.getLogin())) {
            try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO users(login, password, status) VALUES (?, ?, ?)")) {
                stmt.setString(1, newUser.getLogin());
                stmt.setString(2, newUser.getPassword());
                stmt.setString(3, newUser.getStatus());
                stmt.executeUpdate();
            }
        }
    }

    private static boolean userExists(String login) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM users WHERE login = ?")) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }
}