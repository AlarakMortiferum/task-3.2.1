package ru.netology.testmode.data;

import java.sql.*;

public class SQLHelper {
    private static final String url = System.getProperty("jdbc.url", "jdbc:postgresql://localhost:5432/app");
    private static final String user = System.getProperty("jdbc.user", "app");
    private static final String password = System.getProperty("jdbc.password", "pass");

    private SQLHelper() {
    }

    public static String getVerificationCodeFor(User dataUser) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password);
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
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM auth_codes");
            stmt.executeUpdate("DELETE FROM card_transactions");
            stmt.executeUpdate("DELETE FROM cards");
            stmt.executeUpdate("DELETE FROM users");
        }
    }
}