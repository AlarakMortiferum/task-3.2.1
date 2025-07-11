package ru.netology.testmode.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbHelper {

    private static final String url = System.getProperty("jdbc.url");
    private static final String user = System.getProperty("jdbc.user");
    private static final String password = System.getProperty("jdbc.password");

    public static String getLatestAuthCode(String login) throws SQLException {
        String code = null;
        String query = "SELECT ac.code FROM auth_codes ac " +
                "JOIN users u ON ac.user_id = u.id " +
                "WHERE u.login = ? ORDER BY ac.created DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    code = rs.getString("code");
                }
            }
        }
        return code;
    }

    public static void cleanDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM auth_codes;");
            stmt.executeUpdate("DELETE FROM card_transactions;");
            stmt.executeUpdate("DELETE FROM cards;");
            stmt.executeUpdate("DELETE FROM users;");
        }
    }
}
