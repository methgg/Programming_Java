package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {
    private final DatabaseManager databaseManager;

    public SchemaInitializer(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void initialize() throws SQLException {
        try (Connection connection = databaseManager.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id BIGSERIAL PRIMARY KEY,
                        username TEXT NOT NULL UNIQUE,
                        password_hash TEXT NOT NULL
                    )
                    """);
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS music_bands (
                        id BIGSERIAL PRIMARY KEY,
                        collection_key BIGINT NOT NULL UNIQUE,
                        name TEXT NOT NULL,
                        coord_x INTEGER NOT NULL,
                        coord_y DOUBLE PRECISION NOT NULL,
                        creation_date TIMESTAMP NOT NULL,
                        number_of_participants INTEGER NOT NULL,
                        genre TEXT NOT NULL,
                        front_man_name TEXT NOT NULL,
                        front_man_birthday DATE,
                        front_man_height BIGINT,
                        front_man_passport_id TEXT NOT NULL,
                        front_man_eye_color TEXT,
                        owner_id BIGINT NOT NULL REFERENCES users(id)
                    )
                    """);    
        }
    }
}