package lab3.exceptions;
public class NoOwnerException extends RuntimeException {
    public NoOwnerException(String message){
        super(message);
    }
}