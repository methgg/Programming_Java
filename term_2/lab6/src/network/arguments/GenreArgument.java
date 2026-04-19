package network.arguments;

import java.io.Serializable;

import models.MusicGenre;

public class GenreArgument implements Serializable {
    private static final long serialVersionUID = 1L; 
    private final MusicGenre genre;

    public GenreArgument(MusicGenre genre) {
        this.genre = genre;
    } 

    public MusicGenre getGenre(){
        return genre;
    }
}