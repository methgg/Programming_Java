package models;
public class Coordinates {
    private Integer x;
    private Double y;

    public Coordinates(Integer x, Double y){
        if (x == null) {
            throw new IllegalArgumentException("Координата x не может быть null");
        }

        if (y == null) {
            throw new IllegalArgumentException("Координата y не может быть null");
        }

        if (y > 525) {
            throw new IllegalArgumentException("Координата y не может превышать 525");
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