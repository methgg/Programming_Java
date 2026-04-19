package models;

import java.io.Serializable;

import exceptions.ErrorMessages;
import exceptions.ValidationException;

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private java.time.LocalDate birthday; //Поле может быть null
    private Long height; //Поле может быть null, Значение поля должно быть больше 0
    private String passportID; //Строка не может быть пустой, Поле не может быть null
    private Color eyeColor; //Поле может быть null

    public Person(String name, java.time.LocalDate birthday, Long height, String passportID, Color eyeColor) {
        if (name == null) {
            throw new ValidationException(ErrorMessages.PERSON_NAME_NULL);
        }

        if (name.equals("")) {
            throw new ValidationException(ErrorMessages.PERSON_NAME_EMPTY);
        }

        if (height != null && height <= 0) {
            throw new ValidationException(ErrorMessages.HEIGHT_INVALID);
        }

        if (passportID == null){
            throw new ValidationException(ErrorMessages.PASSPORT_NULL);
        }

        if (passportID.equals("")) {
            throw new ValidationException(ErrorMessages.PASSPORT_EMPTY);
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

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday (java.time.LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setHeight (Long height) {
        this.height = height;
    }

    public void setPassportID (String passportID) {
        this.passportID = passportID;
    }

    public void setColor (Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + ", birthday=" + birthday + ", height=" + height + ", passportID='" + passportID + '\'' + ", eyeColor=" + eyeColor + '}';
    }

}
