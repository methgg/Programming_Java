package network.arguments;

import java.io.Serializable;

import models.MusicBand;

public class UpdateArgument implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final MusicBand musicBand;

    public UpdateArgument(Long id, MusicBand musicBand){
        this.id = id;
        this.musicBand = musicBand;
    }

    public Long getId(){
        return id;
    }

    public MusicBand getMusicBand() {
        return musicBand;
    }
}