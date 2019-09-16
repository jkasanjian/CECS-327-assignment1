public class MusicClass {

    private Release release;
    private Artist artist;
    private Song song;


    public MusicClass(Release release, Artist artist, Song song) {
        this.release = release;
        this.artist = artist;
        this.song = song;
    }

    public Release getRelease() {
        return release;
    }

    public Artist getArtist() {
        return artist;
    }

    public Song getSong() {
        return song;
    }
}
