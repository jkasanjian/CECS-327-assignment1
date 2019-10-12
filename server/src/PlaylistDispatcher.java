import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDispatcher {
    private SingletonProfiles profiles;

    private SingletonProfile singletonProfile;

    public PlaylistDispatcher(){
        profiles = SingletonProfiles.GetInstance();
    }

    public String getSongs(String sessionID, String playlistName, String pageNum) throws FileNotFoundException {
        int pageNumber = Integer.parseInt(pageNum);
        System.out.println("Reached PlayList Dispatcher");
        if (playlistName.equals("")) {
            Gson gson = new Gson();
            String fileName = "music.json";
            int songsUpperBound = pageNumber * 20;
            int songsLowerBound = songsUpperBound - 20;
            System.out.println(songsLowerBound + " - " + songsUpperBound);
            Type musicClassType = new TypeToken<ArrayList<MusicClass>>() {
            }.getType();
            List<MusicClass> playlist = gson.fromJson(new FileReader(fileName), musicClassType);
            playlist = playlist.subList(songsLowerBound, songsUpperBound);
            Playlist songPlaylist = new Playlist(playlistName, playlist);

            for (int i = 0; i < playlist.size(); i++) {
                System.out.println(playlist.get(i).getSongTitle());
            }
            return gson.toJson(songPlaylist);
        } else {
            int songsUpperBound = pageNumber * 20;
            int songsLowerBound = songsUpperBound - 20;
            List<MusicClass> playlist = new ArrayList<MusicClass>();
            singletonProfile = SingletonProfile.GetInstance();
            for (Playlist p : singletonProfile.getPlaylists()) {
                if (p.getName().equals(playlistName)) {
                    playlist = p.getMusicClassList();
                }
            }
            playlist = playlist.subList(songsLowerBound, songsUpperBound);
            Gson gson = new Gson();
            return gson.toJson(playlist);
        }
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

    /**
     * @param songId
     * @return MusicClass that is referenced by songId
     */
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
                break;
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
