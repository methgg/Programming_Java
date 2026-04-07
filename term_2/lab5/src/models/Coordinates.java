package models;

import exceptions.ErrorMessages;
import exceptions.ValidationException;

public class Coordinates {
    private Integer x;
    private Double y;

    public Coordinates(Integer x, Double y){
        if (x == null) {
            throw new ValidationException(ErrorMessages.COORDINATE_X_NULL);
        }

        if (y == null) {
            throw new ValidationException(ErrorMessages.COORDINATE_Y_NULL);
        }

        if (y > 525) {
            throw new ValidationException(ErrorMessages.COORDINATE_Y_TOO_LARGE);
        }

        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

        @Override
    public String toString() {
        return "Coordinates{" + "x=" + x + ", y=" + y + '}';
    }

}
