package ru.netology.testmode.data;

import java.sql.*;

public class DbService {
    private final Connection connection;

    public DbService(String url, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users(login, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
        }
    }

    public boolean userExists(String login) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE login = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }
}