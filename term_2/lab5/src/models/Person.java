package models;

/**
 * Модель фронтмена группы.
 */
public class Person {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private java.time.LocalDate birthday; //Поле может быть null
    private Long height; //Поле может быть null, Значение поля должно быть больше 0
    private String passportID; //Строка не может быть пустой, Поле не может быть null
    private Color eyeColor; //Поле может быть null

    public Person(String name, java.time.LocalDate birthday, Long height, String passportID, Color eyeColor) {
        if (name == null) {
            throw new IllegalArgumentException("Имя не может быть null");
        }

        if (name.equals("")) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }

        if (height != null && height <= 0) {
            throw new IllegalArgumentException("Рост должен быть больше 0");
        }

        if (passportID == null){
            throw new IllegalArgumentException("PassportID не может быть null");
        }

        if (passportID.equals("")) {
            throw new IllegalArgumentException("PassportID не может быть пустым");
        }

        this.name = name;
        this.birthday = birthday;
        this.height = height;
        this.passportID = passportID;
        this.eyeColor = eyeColor;
    }

    public String getName() {
        return name;
    }

    public java.time.LocalDate getBirthday() {
        return birthday;
    }

    public Long getHeight() {
        return height;
    }

    public String getPassportID() {
        return passportID;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + ", birthday=" + birthday + ", height=" + height + ", passportID='" + passportID + '\'' + ", eyeColor=" + eyeColor + '}';
    }

}