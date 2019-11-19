package dfs;

import java.io.Serializable;

/**
 * Model class for a song in the music.json file
 */
public class MusicClass implements Serializable {

    private Release release;    // nested object for song release
    private Artist artist;      // nested object for song artist
    private Song song;          // nested onject for song 'song'

    /**
     * Constructor for MusicClass
     * Parameters for each nested json object of each song entry
     * @param release - nested object for release
     * @param artist - nested object for artist
     * @param song - nested object for song
     */
    public MusicClass(Release release, Artist artist, Song song) {
        this.release = release;
        this.artist = artist;
        this.song = song;
    }

    public Release getReleaseObj() {
        return release;
    }

    public Artist getArtistObj() {
        return artist;
    }

    public Song getSongObj() {
        return song;
    }

    /**
     * Gets the song title from the nested object artist
     * @return string with title of song
     */
    public String getSongTitle(){
        return song.getTitle();
    }

    /**
     * Gets the artist name from the nested object artist
     * @return string with name of artist
     */
    public String getArtistName(){
        return artist.getName();
    }

    /**
     * Gets the duration of the music from the nested object song
     * @return double with duration of song in seconds
     */
    public double getDurationTime(){
        return song.getDuration();
    }

    /**
     * Gets the year of the music from the nested object song
     * @return int with the year the music was released
     */
    public int getSongYear(){
        return song.getYear();
    }

    /**
     * Gets the song ID from the nested object song
     * Can be used to play the song, or look up a specific song
     * @return string with the song ID
     */
    public String getSongID(){
        return song.getId();
    }

}
