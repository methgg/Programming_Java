package network.arguments;

import java.io.Serializable;

public class KeyArgument implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Long key;

    public KeyArgument(Long key){
        this.key = key;
    }

    public Long getKey() {
        return key;
    }
}