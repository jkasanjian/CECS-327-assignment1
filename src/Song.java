public class Song {

    private String name;
    private String artist;
    private String duration;
    private String yearReleased;

    // Default Constructor
    public Song(){
        this.name = "N/A";
        this.artist = "N/A";
        this.duration = "N/A";
        this.yearReleased = "N/A";
    }

    // Argument Constructor
    public Song(String name, String artist, String duration, String yearReleased){
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.yearReleased = yearReleased;
    }

    // Get Method for Song Name
    public String getName(){
        return this.name;
    }

    // Set Method for Song Name
    public void setName(String name){
        this.name = name;
    }

    // Get Method for Song Artist
    public String getArtist(){
        return this.artist;
    }

    // Set Method for Song Artist
    public void setArtist(String artist){
        this.artist = artist;
    }

    // Get Method for Song Duration
    public String getDuration(){
        return this.duration;
    }

    // Set Method for Song Duration
    public void setDuration(String duration){
        this.duration = duration;
    }

    // Get Method for Year of Song Release
    public String getYearReleased(){
        return this.yearReleased;
    }

    // Set Method for Year of Song Release
    public void setYearReleased(String yearReleased){
        this.yearReleased = yearReleased;
    }

}
