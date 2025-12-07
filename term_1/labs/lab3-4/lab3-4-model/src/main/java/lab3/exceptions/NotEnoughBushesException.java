package lab3.exceptions;

public class NotEnoughBushesException extends Exception {
    @Override
    public String getMessage() {
        return "Беглецов оказалось больше, чем кустов! Некоторым беглецам негде прятаться.";
    }
}