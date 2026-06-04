package network.arguments;

import java.io.Serializable;

import models.MusicBand;

public class InsertArgument implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Long key;
    private final MusicBand musicBand;

    public InsertArgument(Long key, MusicBand musicBand){
        this.key = key;
        this.musicBand = musicBand;
    }

    public Long getKey(){
        return key;
    }

    public MusicBand getMusicBand() {
        return musicBand;
    }
}