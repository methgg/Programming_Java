package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exceptions.ErrorMessages;

public class DatabaseManager {
    private final DatabaseConfig config;

    public DatabaseManager(DatabaseConfig config) {
        this.config = config;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(config.getJdbcUrl(), config.getUser(), config.getPassword());
    }

    public void testConnection() throws SQLException {
        try (Connection connection = getConnection()) {
            if (!connection.isValid(2)) {
                throw new SQLException(ErrorMessages.DATABASE_CONNECTION_INVALID);
            }
        }
    }
}