package database;

import exceptions.ErrorMessages;

public class DatabaseConfig {
    private final String host;
    private final String database;
    private final String user;
    private final String password;

    public DatabaseConfig(String host, String database, String user, String password) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public static DatabaseConfig fromEnvironment() {
        String host = getEnvOrDefault("DB_HOST", "pg");
        String database = getEnvOrDefault("DB_NAME", "studs");
        String user = getEnvOrDefault("DB_USER", System.getenv("USER"));
        String password = System.getenv("DB_PASSWORD");

        if (user == null || user.isBlank()) {
            throw new IllegalStateException(ErrorMessages.DATABASE_USER_NOT_SET);
        }

        if (password == null || password.isBlank()) {
            throw new IllegalStateException(ErrorMessages.DATABASE_PASSWORD_NOT_SET);
        }

        return new DatabaseConfig(host, database, user, password);
    }

    private static String getEnvOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    public String getJdbcUrl() {
        return "jdbc:postgresql://" + host + ":5432/" + database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}