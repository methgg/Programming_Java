package network.arguments;

import java.io.Serializable;

import models.MusicBand;

public class MusicBandArgument implements Serializable {
    private static final long serialVersionUID = 1L;

    private final MusicBand musicBand;

    public MusicBandArgument(MusicBand musicBand){
        this.musicBand = musicBand;
    }

    public MusicBand getMusicBand() {
        return musicBand;
    }
}