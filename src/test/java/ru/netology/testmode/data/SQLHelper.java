package ru.netology.testmode.data;

import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class SQLHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/app";
    private static final String USER = "app";
    private static final String PASSWORD = "pass";

    private SQLHelper() {}

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void addUserIfNeeded(User user) {
        try {
            if (!userExists(user.getLogin())) {
                addUser(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean userExists(String login) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM users WHERE login = ?")) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private static void addUser(User user) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (login, password, status) VALUES (?, ?, ?)")) {
            stmt.setString(1, user.getLogin());
            stmt.setString(2, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            stmt.setString(3, user.getStatus());
            stmt.executeUpdate();
        }
    }

    public static String getVerificationCodeFor(User user) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT code FROM auth_codes WHERE user_id = (" +
                             "SELECT id FROM users WHERE login = ?" +
                             ") ORDER BY created DESC LIMIT 1")) {
            stmt.setString(1, user.getLogin());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("code");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Verification code not found for user: " + user.getLogin());
    }

    public static void clearDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
            stmt.executeUpdate("TRUNCATE TABLE auth_codes");
            stmt.executeUpdate("TRUNCATE TABLE card_transactions");
            stmt.executeUpdate("TRUNCATE TABLE cards");
            stmt.executeUpdate("TRUNCATE TABLE users");
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}