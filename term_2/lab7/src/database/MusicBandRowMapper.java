package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import models.Color;
import models.Coordinates;
import models.MusicBand;
import models.MusicGenre;
import models.Person;

public class MusicBandRowMapper {
    public MusicBand map(ResultSet resultSet) throws SQLException {
        Coordinates coordinates = new Coordinates(
                resultSet.getInt("coord_x"),
                resultSet.getDouble("coord_y")
        );

        LocalDate birthday = resultSet.getDate("front_man_birthday") != null
                ? resultSet.getDate("front_man_birthday").toLocalDate()
                : null;

        Long height = resultSet.getObject("front_man_height", Long.class);

        String eyeColorValue = resultSet.getString("front_man_eye_color");
        Color eyeColor = eyeColorValue != null ? Color.valueOf(eyeColorValue) : null;

        Person frontMan = new Person(
                resultSet.getString("front_man_name"),
                birthday,
                height,
                resultSet.getString("front_man_passport_id"),
                eyeColor
        );

        LocalDateTime creationDate = resultSet.getTimestamp("creation_date").toLocalDateTime();

        return new MusicBand(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                coordinates,
                creationDate,
                resultSet.getInt("number_of_participants"),
                MusicGenre.valueOf(resultSet.getString("genre")),
                frontMan
        );
    }
}
