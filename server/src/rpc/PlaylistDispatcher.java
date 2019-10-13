package rpc;

import com.google.gson.Gson;
import model.*;
import model.Playlist;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PlaylistDispatcher {
    private ProfileDatabase profiles;

    public PlaylistDispatcher(){
        profiles = ProfileDatabase.GetInstance();
    }

    public String getSongs(String sessionID, String playlistName, String pageNum) throws FileNotFoundException {
        int pageNumber = Integer.parseInt(pageNum);
        if (playlistName.equals("")) {
            return new Gson().toJson(new Playlist(playlistName,
                    MusicDatabase.GetInstance().getSongs(pageNumber-1)));
        } else {
            return getPage(sessionID, playlistName, pageNumber-1);
        }
    }

    public String createPlaylist( String sessionID, String playlistName ) throws IOException {
        profiles.createPlaylist(Integer.parseInt(sessionID), playlistName);
        return new Gson().toJson(new ProfileAccount());
    }

    public String deletePlaylist( String sessionID, String playlistName ) throws IOException {
        profiles.deletePlaylist(Integer.parseInt(sessionID), playlistName);
        return new Gson().toJson(new ProfileAccount());
    }

    public String addSongToPlaylist( String sessionID, String playlistName, String songId ) throws IOException {
        profiles.addSongToPlaylist(Integer.parseInt(sessionID), playlistName, songId);
        return new Gson().toJson(new ProfileAccount());
    }
    public String getPage( String sessionID, String playlistName, int index ) throws FileNotFoundException {
        Playlist playlist = new Playlist(playlistName, profiles.getPage(Integer.parseInt(sessionID), playlistName, index));
        return new Gson().toJson(playlist);
    }
}
