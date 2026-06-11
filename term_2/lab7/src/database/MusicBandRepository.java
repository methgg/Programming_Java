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

    public MusicBand insert(Long key, MusicBand musicBand, Long ownerId) throws SQLException {
        String sql = """
                INSERT INTO music_bands (
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
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        try (
                Connection connection = databaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, key);
            statement.setString(2, musicBand.getName());
            statement.setInt(3, musicBand.getCoordinates().getX());
            statement.setDouble(4, musicBand.getCoordinates().getY());
            statement.setTimestamp(5, java.sql.Timestamp.valueOf(musicBand.getCreationDate()));
            statement.setInt(6, musicBand.getNumberOfParticipants());
            statement.setString(7, musicBand.getGenre().name());
            statement.setString(8, musicBand.getFrontMan().getName());

            if (musicBand.getFrontMan().getBirthday() != null) {
                statement.setDate(9, java.sql.Date.valueOf(musicBand.getFrontMan().getBirthday()));
            } else {
                statement.setNull(9, java.sql.Types.DATE);
            }

            if (musicBand.getFrontMan().getHeight() != null) {
                statement.setLong(10, musicBand.getFrontMan().getHeight());
            } else {
                statement.setNull(10, java.sql.Types.BIGINT);
            }

            statement.setString(11, musicBand.getFrontMan().getPassportID());

            if (musicBand.getFrontMan().getEyeColor() != null) {
                statement.setString(12, musicBand.getFrontMan().getEyeColor().name());
            } else {
                statement.setNull(12, java.sql.Types.VARCHAR);
            }

            statement.setLong(13, ownerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Long generatedId = resultSet.getLong("id");
                    return new MusicBand(
                            generatedId,
                            musicBand.getName(),
                            musicBand.getCoordinates(),
                            musicBand.getCreationDate(),
                            musicBand.getNumberOfParticipants(),
                            musicBand.getGenre(),
                            musicBand.getFrontMan()
                    );
                }
            }
        }

        throw new SQLException("Не удалось получить id вставленного объекта.");
    }

    public Long findOwnerIdByKey(Long key) throws SQLException {
        String sql = "SELECT owner_id FROM music_bands WHERE collection_key = ?";

        try (
                Connection connection = databaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, key);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("owner_id");
                }
                return null;
            }
        }
    }

    public boolean isOwner(Long key, Long userId) throws SQLException {
        Long ownerId = findOwnerIdByKey(key);
        return ownerId != null && ownerId.equals(userId);
    }

    public boolean deleteByKey(Long key) throws SQLException {
        String sql = "DELETE FROM music_bands WHERE collection_key = ?";

        try (
                Connection connection = databaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, key);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean updateById(Long id, MusicBand musicBand) throws SQLException {
        String sql = """
                UPDATE music_bands
                SET
                    name = ?,
                    coord_x = ?,
                    coord_y = ?,
                    number_of_participants = ?,
                    genre = ?,
                    front_man_name = ?,
                    front_man_birthday = ?,
                    front_man_height = ?,
                    front_man_passport_id = ?,
                    front_man_eye_color = ?
                WHERE id = ?
                """;

        try (
                Connection connection = databaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, musicBand.getName());
            statement.setInt(2, musicBand.getCoordinates().getX());
            statement.setDouble(3, musicBand.getCoordinates().getY());
            statement.setInt(4, musicBand.getNumberOfParticipants());
            statement.setString(5, musicBand.getGenre().name());
            statement.setString(6, musicBand.getFrontMan().getName());

            if (musicBand.getFrontMan().getBirthday() != null) {
                statement.setDate(7, java.sql.Date.valueOf(musicBand.getFrontMan().getBirthday()));
            } else {
                statement.setNull(7, java.sql.Types.DATE);
            }

            if (musicBand.getFrontMan().getHeight() != null) {
                statement.setLong(8, musicBand.getFrontMan().getHeight());
            } else {
                statement.setNull(8, java.sql.Types.BIGINT);
            }

            statement.setString(9, musicBand.getFrontMan().getPassportID());

            if (musicBand.getFrontMan().getEyeColor() != null) {
                statement.setString(10, musicBand.getFrontMan().getEyeColor().name());
            } else {
                statement.setNull(10, java.sql.Types.VARCHAR);
            }

            statement.setLong(11, id);

            return statement.executeUpdate() > 0;
        }
    }

    public Long findOwnerIdByBandId(Long id) throws SQLException {
        String sql = "SELECT owner_id FROM music_bands WHERE id = ?";

        try (
                Connection connection = databaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("owner_id");
                }
                return null;
            }
        }
    }

    public boolean isOwnerByBandId(Long id, Long userId) throws SQLException {
        Long ownerId = findOwnerIdByBandId(id);
        return ownerId != null && ownerId.equals(userId);
    }

    public boolean updateByKey(Long key, MusicBand musicBand) throws SQLException {
        String sql = """
                UPDATE music_bands
                SET
                    name = ?,
                    coord_x = ?,
                    coord_y = ?,
                    number_of_participants = ?,
                    genre = ?,
                    front_man_name = ?,
                    front_man_birthday = ?,
                    front_man_height = ?,
                    front_man_passport_id = ?,
                    front_man_eye_color = ?
                WHERE collection_key = ?
                """;

        try (
                Connection connection = databaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, musicBand.getName());
            statement.setInt(2, musicBand.getCoordinates().getX());
            statement.setDouble(3, musicBand.getCoordinates().getY());
            statement.setInt(4, musicBand.getNumberOfParticipants());
            statement.setString(5, musicBand.getGenre().name());
            statement.setString(6, musicBand.getFrontMan().getName());

            if (musicBand.getFrontMan().getBirthday() != null) {
                statement.setDate(7, java.sql.Date.valueOf(musicBand.getFrontMan().getBirthday()));
            } else {
                statement.setNull(7, java.sql.Types.DATE);
            }

            if (musicBand.getFrontMan().getHeight() != null) {
                statement.setLong(8, musicBand.getFrontMan().getHeight());
            } else {
                statement.setNull(8, java.sql.Types.BIGINT);
            }

            statement.setString(9, musicBand.getFrontMan().getPassportID());

            if (musicBand.getFrontMan().getEyeColor() != null) {
                statement.setString(10, musicBand.getFrontMan().getEyeColor().name());
            } else {
                statement.setNull(10, java.sql.Types.VARCHAR);
            }

            statement.setLong(11, key);

            return statement.executeUpdate() > 0;
        }
    }

    public int deleteAllByOwnerId(Long ownerId) throws SQLException {
        String sql = "DELETE FROM music_bands WHERE owner_id = ?";

        try (
                Connection connection = databaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, ownerId);
            return statement.executeUpdate();
        }
    }

    public int deleteByKeys(Iterable<Long> keys) throws SQLException {
        String sql = "DELETE FROM music_bands WHERE collection_key = ?";

        int deletedCount = 0;

        try (
                Connection connection = databaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            for (Long key : keys) {
                statement.setLong(1, key);
                deletedCount += statement.executeUpdate();
            }
        }

        return deletedCount;
    }

    public int deleteByBandIds(Iterable<Long> ids) throws SQLException {
        String sql = "DELETE FROM music_bands WHERE id = ?";

        int deletedCount = 0;

        try (
                Connection connection = databaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            for (Long id : ids) {
                statement.setLong(1, id);
                deletedCount += statement.executeUpdate();
            }
        }

        return deletedCount;
    }



}





