package ru.netology.testmode.data;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

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
        QueryRunner runner = new QueryRunner();
        String checkSql = "SELECT COUNT(*) FROM users WHERE login = ?";

        try (Connection conn = getConnection()) {
            Long count = runner.query(conn, checkSql, new ScalarHandler<>(), login);
            return count != null && count > 0;
        }
    }

    private static void addUser(User user) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String insertSql = "INSERT INTO users (id, login, password, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            runner.update(conn, insertSql,
                    UUID.randomUUID().toString(),
                    user.getLogin(),
                    BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()),
                    user.getStatus());
        }
    }

    public static String getVerificationCodeFor(User user) {
        QueryRunner runner = new QueryRunner();
        String sql = "SELECT code FROM auth_codes WHERE user_id = (" +
                "SELECT id FROM users WHERE login = ?" +
                ") ORDER BY created DESC LIMIT 1";

        try (Connection conn = getConnection()) {
            return runner.query(conn, sql, new ScalarHandler<>(), user.getLogin());
        } catch (SQLException e) {
            throw new RuntimeException("Verification code not found for user: " + user.getLogin(), e);
        }
    }

    public static void clearDatabase() {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = getConnection()) {
            runner.update(conn, "SET FOREIGN_KEY_CHECKS = 0");
            runner.update(conn, "TRUNCATE TABLE auth_codes");
            runner.update(conn, "TRUNCATE TABLE card_transactions");
            runner.update(conn, "TRUNCATE TABLE cards");
            runner.update(conn, "TRUNCATE TABLE users");
            runner.update(conn, "SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateUserStatus(String login, String status) {
        QueryRunner runner = new QueryRunner();
        String sql = "UPDATE users SET status = ? WHERE login = ?";

        try (Connection conn = getConnection()) {
            runner.update(conn, sql, status, login);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}