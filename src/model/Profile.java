package model;

public class Profile {

    private String username;
    private String password;
    private Playlist playlist;

    public Profile(String username, String password, Playlist playlist) {
        this.username = username;
        this.password = password;
        this.playlist = playlist;
    }

    public Profile(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

}
