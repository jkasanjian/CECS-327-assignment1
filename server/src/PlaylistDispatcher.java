import com.google.gson.Gson;
import javafx.collections.FXCollections;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDispatcher {
    private SingletonProfiles profiles;

    public PlaylistDispatcher() {
        profiles = SingletonProfiles.GetInstance();
    }

    public String createPlaylist( String username, String playlistName ){
        Playlist playlist = new Playlist(playlistName, new ArrayList<>());
        ProfileAccount profile = profiles.getProfile(username);
        profile.addNewPlaylist(playlistName, FXCollections.observableArrayList(playlist.getMusicClassList()));
        profiles.sync(profile);
        return new Gson().toJson(profile);
    }

    public String deletePlaylist( String username, String playlistName ){
        ProfileAccount profile = profiles.getProfile(username);
        profile.removePlaylist(playlistName);
        profiles.sync(profile);
        return new Gson().toJson(profile);
    }

    public String addSongToPlaylist( String username, String playlistName, String songId ){
        ProfileAccount profile = profiles.getProfile(username);
        profile.addToPlaylist(playlistName, getMusicClass(songId));
        profiles.sync(profile);
        return new Gson().toJson(profile);
    }

    private MusicClass getMusicClass(String songId) {
        return null;
    }

    // This has not been implemented before RPC.
//    public String removeSongFromPlaylist( String username, String playlistName, String songId ){
//        ProfileAccount profile = profiles.getProfile(username);
//        profile.removePlaylist(playlistName);
//    }

    public String getPage( String username, String playlistName, int index ){
        ProfileAccount profile = profiles.getProfile(username);
        Playlist playlist = profile.getPlaylist(playlistName);
        List<MusicClass> list = new ArrayList<>();
        for(int i = index; i < index+20; i++) {
            try {
                list.add(playlist.getMusicClassList().get(i));
            } catch (Exception e) {

            }
        }
        return new Gson().toJson(list);
    }

    public String search( String username, String playlistName, String query){
        ProfileAccount profile = profiles.getProfile(username);
        Playlist playlist = profile.getPlaylist(playlistName);
        List<MusicClass> list = new ArrayList<>();
        for(MusicClass musicClass : playlist.getMusicClassList()) {
            if (musicClass.getSongTitle().contains(query))
                list.add(musicClass);
        }
        return new Gson().toJson(list);
    }
}
