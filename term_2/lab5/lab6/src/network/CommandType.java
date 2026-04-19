package network;

import java.io.Serializable;

public enum CommandType implements Serializable{
    HELP,
    INFO,
    SHOW,
    INSERT,
    UPDATE,
    REMOVE_KEY,
    CLEAR,
    EXIT,
    REMOVE_LOWER,
    REPLACE_IF_GREATER,
    REMOVE_GREATER_KEY,
    COUNT_GREATER_THAN_NUMBER_OF_PARTICIPANTS,
    FILTER_GREATER_THAN_GENRE,
    PRINT_FIELD_DESCENDING_FRONT_MAN,
    SHOW_EVEN
}