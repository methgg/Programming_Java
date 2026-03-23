package models;

import java.time.LocalDateTime;
import util.IdGenerator;

/**
 * Модель музыкальной группы, хранимая в коллекции.
 */
public class MusicBand implements Comparable<MusicBand> {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer numberOfParticipants; //Поле не может быть null, Значение поля должно быть больше 0
    private MusicGenre genre; //Поле не может быть null
    private Person frontMan; //Поле не может быть null

    public MusicBand(String name, Coordinates coordinates, Integer numberOfParticipants, MusicGenre genre, Person frontMan) {
        if (name == null) {
            throw new IllegalArgumentException("Имя не может быть null");
        }

        if (name.equals("")) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }

        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть null");
        }
        if (numberOfParticipants == null || numberOfParticipants <= 0) {
            throw new IllegalArgumentException("Количество участников должно быть больше 0");
        }

        if (genre == null) {
            throw new IllegalArgumentException("Жанр не может быть null");
        }

        if (frontMan == null) {
            throw new IllegalArgumentException("FrontMan не может быть null");
        }

        this.id = IdGenerator.generateId();
        this.creationDate = LocalDateTime.now();

        this.name = name;
        this.coordinates = coordinates;
        this.numberOfParticipants = numberOfParticipants;
        this.genre = genre;
        this.frontMan = frontMan;
        }

        @Override
        public int compareTo(MusicBand other) {
            return this.numberOfParticipants.compareTo(other.numberOfParticipants);
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Coordinates getCoordinates() {
            return coordinates;
        }

        public LocalDateTime getCreationDate() {
            return creationDate;
        }

        public Integer getNumberOfParticipants() {
            return numberOfParticipants;
        }

        public MusicGenre getGenre() {
            return genre;
        }

        public Person getFrontMan() {
            return frontMan;
        }

        @Override
        public String toString() {
        return "MusicBand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", numberOfParticipants=" + numberOfParticipants +
                ", genre=" + genre +
                ", frontMan=" + frontMan +
                '}';
}
}