package network;

import java.io.Serializable;

public class CommandResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final String message;
    private final Serializable data;

    public CommandResponse(boolean success, String message, Serializable data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess(){
        return success;
    } 

    public String getMessage() {
        return message;
    }

    public Serializable getData(){
        return data;
    }
}