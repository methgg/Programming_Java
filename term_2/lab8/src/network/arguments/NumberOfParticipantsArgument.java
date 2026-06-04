package network.arguments;

import java.io.Serializable;

public class NumberOfParticipantsArgument implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Integer numberOfParticipants;

    public NumberOfParticipantsArgument(Integer numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }
}
