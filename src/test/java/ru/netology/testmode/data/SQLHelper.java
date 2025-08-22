package ru.netology.testmode.data;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/app";
    private static final String USER = "app";
    private static final String PASSWORD = "pass";

    private SQLHelper() {}

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void addUserIfNeeded(User user) {
        QueryRunner runner = new QueryRunner();
        String checkSql = "SELECT COUNT(*) FROM users WHERE login = ?";

        try (Connection conn = getConnection()) {
            Long count = runner.query(conn, checkSql, new ScalarHandler<>(), user.getLogin());
            if (count == 0) {
                String insertSql = "INSERT INTO users (login, password, status) VALUES (?, ?, ?)";
                runner.update(conn, insertSql,
                        user.getLogin(),
                        BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()),
                        user.getStatus());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getVerificationCodeFor(User user) {
        QueryRunner runner = new QueryRunner();
        String sql = "SELECT code FROM auth_codes WHERE user_id = " +
                "(SELECT id FROM users WHERE login = ?) ORDER BY created DESC LIMIT 1";

        try (Connection conn = getConnection()) {
            return runner.query(conn, sql, new ScalarHandler<>(), user.getLogin());
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
}