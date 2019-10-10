package model;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileAccount {

    private String username;
    private String password;
    private List<Playlist> playlists;

    public ProfileAccount() {
        username = "";
        password = "";
        playlists = new ArrayList<>();
    }

    public ProfileAccount(String username, String password, List<Playlist> playlists) {
        this.username = username;
        this.password = password;
        this.playlists = playlists;
    }

    public ProfileAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.playlists = new ArrayList<>();
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

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    /**
     * Adds a song to the List<Playlist> playlists attribute
     * @param playlistName name of the playlist
     * @param selectedSong song to add to the playlist
     */
    public void addToPlaylist(String playlistName, MusicClass selectedSong) {
        for (Playlist playlist : playlists) {
            if (playlist.getName().equals(playlistName)) {
                playlist.addSong(selectedSong);
                return;
            }
        }
        try {
            throw new Exception("Play list to be modified not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds new playlist to the List<Playlist> playlists attribute
     * @param playlistName name of the playlist
     * @param playlistSongs contents of the playlist as an ObservableList<MusicClass>
     */
    public void addNewPlaylist(String playlistName, ObservableList<MusicClass> playlistSongs) {
        List<MusicClass> playlistSongsAsList = playlistSongs.stream().collect(Collectors.toList());
        playlists.add(new Playlist(playlistName, playlistSongsAsList));
    }

    /**
     * Removes playlist from List<Playlist> playlists attribute
     * @param playlistName the name of the playlist to be removed
     */
    public void removePlaylist(String playlistName) {
        boolean gotRemoved = playlists.removeIf(x -> x.getName().equals(playlistName));
        if (!gotRemoved) {
            try {
                throw new Exception("Play list to be removed not found!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "username: " + username + "\n"
                + "password: " + password + "\n"
                + "playlist: " + playlists;
    }
}
