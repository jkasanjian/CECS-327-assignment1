import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import model.MusicClass;
import model.Playlist;
import model.SingletonProfile;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDispatcher {

    SingletonProfile singletonProfile;

    public PlaylistDispatcher(){}

    public String getSongs(String sessionID, String playlistName, String pageNum) throws FileNotFoundException {
        int pageNumber = Integer.parseInt(pageNum);
        System.out.println("Reached PlayList Dispatcher");
        if(playlistName.equals("")){
            Gson gson = new Gson();
            String fileName = "music.json";
            int songsUpperBound = pageNumber*20;
            int songsLowerBound = songsUpperBound-20;
            System.out.println(songsLowerBound + " - " + songsUpperBound);
            Type musicClassType = new TypeToken<ArrayList<MusicClass>>() {}.getType();
            List<MusicClass> playlist = gson.fromJson(new FileReader(fileName), musicClassType);
            playlist = playlist.subList(songsLowerBound, songsUpperBound);
            Playlist songPlaylist = new Playlist(playlistName, playlist);

            for(int i = 0; i < playlist.size(); i++){
                System.out.println(playlist.get(i).getSongTitle());
            }
            return gson.toJson(songPlaylist);
        }else{
            int songsUpperBound = pageNumber*20;
            int songsLowerBound = songsUpperBound-20;
            List<MusicClass> playlist = new ArrayList<MusicClass>();
            singletonProfile = SingletonProfile.GetInstance();
            for (Playlist p : singletonProfile.getPlaylists()) {
                if (p.getName().equals(playlistName)){
                    playlist = p.getMusicClassList();
                }
            }
            playlist = playlist.subList(songsLowerBound, songsUpperBound);
            Gson gson = new Gson();
            return gson.toJson(playlist);
        }
    }
}
