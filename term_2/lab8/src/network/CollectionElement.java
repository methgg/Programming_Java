package network;

import java.io.Serializable;

import models.MusicBand;

public class CollectionElement implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Long key;
    private final MusicBand musicBand;
    private final String ownerUsername;

    public CollectionElement(Long key, MusicBand musicBand, String ownerUsername) {
        this.key = key;
        this.musicBand = musicBand;
        this.ownerUsername = ownerUsername;
    }

    public Long getKey() {
        return key;
    }

    public MusicBand getMusicBand() {
        return musicBand;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }
}
