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

    public String getSongs(String sessionID, String playlistName, String pageNum) throws Exception {
        int pageNumber = Integer.parseInt(pageNum);
        if (playlistName.equals("")) {
            return new Gson().toJson(new Playlist(playlistName,
                    MusicDatabase.GetInstance().getSongs(pageNumber-1)));
        } else {
            return getPage(sessionID, playlistName, pageNumber-1);
        }
    }

    public String createPlaylist( String sessionID, String playlistName ) throws Exception {
        profiles.createPlaylist(Integer.parseInt(sessionID), playlistName);
        return new Gson().toJson(new ProfileAccount());
    }

    public String deletePlaylist( String sessionID, String playlistName ) throws Exception {
        profiles.deletePlaylist(Integer.parseInt(sessionID), playlistName);
        return new Gson().toJson(new ProfileAccount());
    }

    public String addSongToPlaylist( String sessionID, String playlistName, String songId ) throws Exception {
        profiles.addSongToPlaylist(Integer.parseInt(sessionID), playlistName, songId);
        return new Gson().toJson(new ProfileAccount());
    }
    public String getPage( String sessionID, String playlistName, int index ) throws Exception {
        Playlist playlist = new Playlist(playlistName, profiles.getPage(Integer.parseInt(sessionID), playlistName, index));
        return new Gson().toJson(playlist);
    }


    public String search(String sessionID, String playlistName, String query, int pageNumber) throws Exception {
        //System.out.println("IN PLAYLIST DISPATCHER " + playlistName + query);
        if (playlistName.equals("")) {
            //System.out.println("IN PLAYLIST DISPATCHER: all songs ");
            return new Gson().toJson(new Playlist(playlistName,
                    MusicDatabase.GetInstance().getSongsSearch(pageNumber - 1, query)));
        } else {
            //System.out.println("IN PLAYLIST DISPATCHER: searching " + playlistName);
            return getSearch(sessionID, playlistName, query, pageNumber-1);
        }

    }


    public String getSearch(String sessionID, String playlistName, String query, int index) throws Exception {
        Playlist playlist = new Playlist(playlistName, profiles.getPageSearch(Integer.parseInt(sessionID), playlistName, query, index));
        return new Gson().toJson(playlist);
    }


}
