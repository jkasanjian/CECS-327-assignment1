
import model.SingletonProfiles;

public class PlaylistDispatcher {

    private SingletonProfiles singletonProfiles;

    public PlaylistDispatcher(){
        singletonProfiles = SingletonProfiles.GetInstance();
    }
    public String createPlaylist(String username, String playlistName) {
        return null;
    }
}
