package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import models.MusicBand;

public class MusicBandRepository {
    private final DatabaseManager databaseManager;
    private final MusicBandRowMapper rowMapper;

    public MusicBandRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.rowMapper = new MusicBandRowMapper();
    }

    public LinkedHashMap<Long, MusicBand> loadAll() throws SQLException {
        LinkedHashMap<Long, MusicBand> collection = new LinkedHashMap<>();

        String sql = """
                SELECT
                    id,
                    collection_key,
                    name,
                    coord_x,
                    coord_y,
                    creation_date,
                    number_of_participants,
                    genre,
                    front_man_name,
                    front_man_birthday,
                    front_man_height,
                    front_man_passport_id,
                    front_man_eye_color,
                    owner_id
                FROM music_bands
                ORDER BY collection_key
                """;
        try (
                Connection connection = databaseManager.getConnection(); 
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Long key = resultSet.getLong("collection_key");
                MusicBand musicBand = rowMapper.map(resultSet);
                collection.put(key, musicBand);
            }
        }

        return collection;
    }
}