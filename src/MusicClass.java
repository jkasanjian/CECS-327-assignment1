public class MusicClass {

    private Release release;
    private Artist artist;
    private Song song;


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

    public String getSongTitle(){
        return song.getTitle();
    }

    public String getArtistName(){
        return artist.getName();
    }

    public double getDurationTime(){
        return song.getDuration();
    }

    public int getSongYear(){
        return song.getYear();
    }

    public String getSongID(){
        return song.getId();
    }

}
