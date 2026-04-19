package network.arguments;

import java.io.Serializable;

public class IdArgument implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Long id;

    public IdArgument(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}