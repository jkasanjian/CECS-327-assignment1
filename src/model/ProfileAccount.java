package model;

public class ProfileAccount {

    private String username;
    private String password;
    private Playlist playlist;

    public ProfileAccount(String username, String password, Playlist playlist) {
        this.username = username;
        this.password = password;
        this.playlist = playlist;
    }

    public ProfileAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.playlist = null;
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
