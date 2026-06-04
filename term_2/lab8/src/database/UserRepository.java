package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private final DatabaseManager databaseManager;

    public UserRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";

        try (
                Connection connection = databaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public void createUser(String username, String passwordHash) throws SQLException {
        String sql = "INSERT INTO users(username, password_hash) VALUES (?, ?)";

        try (
                Connection connection = databaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);
            statement.setString(2, passwordHash);
            statement.executeUpdate();
        }
    }

    public boolean isValidUser(String username, String passwordHash) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ? AND password_hash = ?";

        try (
                Connection connection = databaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);
            statement.setString(2, passwordHash);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public Long findUserIdByUsername(String username) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (
                Connection connection = databaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                }
                return null;
            }
        }
    }
    
}