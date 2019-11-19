package model;

import com.google.gson.Gson;
import dfs.DFS;
import dfs.RemoteInputFileStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rpc.SessionManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileDatabase {
    private static ProfileDatabase profileDatabase = null;
    private final String FILE_NAME = "ProfilesJson";
    private final String PROFILE_REGEX = "(\\[?|\\,)(?=\\{\"username\":)";
    private final int PAGE_SIZE = 20;
    private DFS dfs;

    private ProfileDatabase(){}

    private boolean doesFileExists() {
//        File file = new File(FILE_NAME);
//        return file.exists();
        return true;
    }

    public static ProfileDatabase GetInstance() {
        if(profileDatabase==null)
            profileDatabase = new ProfileDatabase();
        return profileDatabase;
    }

    public void setDFS(DFS dfs){
        this.dfs = dfs;
    }

    public boolean containsUsername(String username) throws Exception {
        if(!doesFileExists()) return false;
        RemoteInputFileStream fileInputStream = dfs.read(FILE_NAME, 1);
        //fileInputStream.connect();
        Scanner scanner = new Scanner(fileInputStream).useDelimiter(PROFILE_REGEX);
        while(scanner.hasNext()) {
            String token = scanner.next().toLowerCase();
            Pattern pattern = Pattern.compile("(?<=\"username\":\")" + username.toLowerCase() + "(?=\",\"password\":)");
            Matcher matcher = pattern.matcher(token);
            if(matcher.find())
                return true;
        }
        return false;
    }

    public boolean verifyLogin(String username, String password) throws Exception {
        if(!doesFileExists()) return false;
        System.out.println(" bBBBBBBBBBBBB");
        RemoteInputFileStream fileInputStream = dfs.read(FILE_NAME, 1);
        //fileInputStream.connect();
        System.out.println(fileInputStream + " AAAAAAAAAAAAAAAAAAA");
        Scanner scanner = new Scanner(fileInputStream).useDelimiter(PROFILE_REGEX);
        while(scanner.hasNext()) {
            String token = scanner.next();
            Pattern pattern = Pattern.compile("(?<=\"username\":\")" + username.toLowerCase() + "(?=\",\"password\":)");
            Matcher matcher = pattern.matcher(token.toLowerCase());
            if(matcher.find()) {
                Pattern pattern1 = Pattern.compile("(?<=,\"password\":\")" + password + "\",(\"sessionID\"\\S+)?\"playlists\"");
                Matcher matcher1 = pattern1.matcher(token);
                if(matcher1.find())
                    return true;
                return false;
            }
        }
        return false;
    }

    private ProfileAccount getProfile(String username) throws Exception {
        if(!doesFileExists()) return null;
        RemoteInputFileStream fileInputStream = dfs.read(FILE_NAME, 1);
        //fileInputStream.connect();
        Scanner scanner = new Scanner(fileInputStream).useDelimiter(PROFILE_REGEX);
        while(scanner.hasNext()) {
            StringBuilder token = new StringBuilder(scanner.next());
            Pattern pattern = Pattern.compile("(?<=\"username\":\")" + username.toLowerCase() + "(?=\",\"password\":)");
            Matcher matcher = pattern.matcher(token.toString().toLowerCase());
            if(matcher.find()) {
                while(token.charAt(token.length()-1) != '}')
                    token.deleteCharAt(token.length()-1);
                return new Gson().fromJson(token.toString(), ProfileAccount.class);
            }
        }
        return null;
    }

    private ProfileAccount getProfile(int sessionID) throws Exception {
        if(!doesFileExists()) return null;
        ProfileAccount profileAccount = getProfile(new SessionManager().getActiveUsername(sessionID));
        return new ProfileAccount(profileAccount.getUsername(), profileAccount.getPassword(), profileAccount.getPlaylists());
    }

    private void sync(ProfileAccount oldProfile, ProfileAccount newProfile) throws IOException {
        String oldProf = new Gson().toJson(new ProfileAccount(oldProfile.getUsername(),
                oldProfile.getPassword(), oldProfile.getPlaylists()));
        String newProf = new Gson().toJson(new ProfileAccount(newProfile.getUsername(),
                newProfile.getPassword(), newProfile.getPlaylists()));
        BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME));
        String st;
        StringBuilder ret = new StringBuilder();
        while ((st = bufferedReader.readLine()) != null)
            ret.append(st + "\n");
        st = ret.toString();
        st = st.replace(oldProf, newProf);
        FileWriter fileWriter = new FileWriter(FILE_NAME);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(st);
        bufferedWriter.close();
        fileWriter.close();
    }

    public void deletePlaylist(String username, String playlistName) throws Exception {
        ProfileAccount newProfile = getProfile(username);
        ProfileAccount oldProfile = getProfile(username);
        if(newProfile==null) return;
        if(newProfile.getPlaylist(playlistName) == null) return;
        newProfile.removePlaylist(playlistName);
        sync(oldProfile, newProfile);
    }

    public void deletePlaylist(int sessionID, String playlistName) throws Exception {
        ProfileAccount newProfile = getProfile(sessionID);
        if(newProfile==null) return;
        if(newProfile.getPlaylist(playlistName) == null) return;
        newProfile.removePlaylist(playlistName);
        sync(getProfile(sessionID), newProfile);
    }

    public void createPlaylist(String username, String playlistName) throws Exception {
        ProfileAccount profileAccount = getProfile(username);
        if(!containsUsername(username)) return;
        for(Playlist playlist : profileAccount.getPlaylists())
            if(playlist.getName().equals(playlistName)) return;
        profileAccount.addNewPlaylist(playlistName, FXCollections.observableArrayList());
        sync(getProfile(username), profileAccount);
    }

    public void createPlaylist(int sessionID, String playlistName) throws Exception {
        ProfileAccount profileAccount = getProfile(sessionID);
        System.out.println(profileAccount.getUsername()+ " LOL");
        if(!containsUsername(profileAccount.getUsername())) return;
        for(Playlist playlist : profileAccount.getPlaylists())
            if(playlist.getName().equals(playlistName)) return;
        profileAccount.addNewPlaylist(playlistName, FXCollections.observableArrayList());
        System.out.println("About to sync this ...fuck its 12 midnight.");
        sync(getProfile(sessionID), profileAccount);
    }

    public void addProfile(ProfileAccount profileAccount) throws Exception {
        if(containsUsername(profileAccount.getUsername())) return;
        if(!doesFileExists()) {
            File file = new File(FILE_NAME);
            file.createNewFile();
        }
        BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME));
        String st;
        String ret = "";
        while ((st = bufferedReader.readLine()) != null)
            ret += (st + "\n");
        int exists = ret.lastIndexOf("]");
        if(exists != -1) ret = ret.substring(0, ret.lastIndexOf("]"));
        if(exists != -1)
            ret += ("," + new Gson().toJson(new ProfileAccount(profileAccount.getUsername(),
                    profileAccount.getPassword(), profileAccount.getPlaylists())) + "]");
        else
            ret += ("[" + new Gson().toJson(new ProfileAccount(profileAccount.getUsername(),
                    profileAccount.getPassword(), profileAccount.getPlaylists())) + "]");
        FileWriter fileWriter = new FileWriter(FILE_NAME);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(ret);
        bufferedWriter.close();
        fileWriter.close();
    }

    public List<MusicClass> getPage(int sessionID, String playlistName, int index) throws Exception {
        ProfileAccount profileAccount = getProfile(sessionID);
        if(profileAccount.getPlaylist(playlistName)==null) return null;
        List<MusicClass> ret = new ArrayList<>();
        int max = PAGE_SIZE;
        while(true) {
            try {
                ret = profileAccount.getPlaylist(playlistName).getMusicClassList().subList(PAGE_SIZE*index, PAGE_SIZE*index + max);
                break;
            } catch (Exception e) {
                max--;
            } finally {
                if(max == 0)
                    break;
            }
        }
        return ret;
    }

    public List<MusicClass> getPageSearch(int sessionID, String playlistName, String query, int index) throws Exception {
        ProfileAccount profileAccount = getProfile(sessionID);
        if(profileAccount.getPlaylist(playlistName)==null) return null;
        List<MusicClass> searchedPlaylist = new ArrayList<>();
        List<MusicClass> playlist = profileAccount.getPlaylist(playlistName).getMusicClassList();

        // check if playlist contains query in song name or artist
        query = query.toLowerCase();
        for(MusicClass song : playlist){
            if(song.getSongTitle().toLowerCase().contains(query)){
                searchedPlaylist.add(song);
            }
            if(song.getArtistName().toLowerCase().contains(query)){
                searchedPlaylist.add(song);
            }
        }
        // searchedPlaylist now only has matches
        List<MusicClass> ret = new ArrayList<>();
        int max = PAGE_SIZE;
        while(true) {
            try {
                ret = searchedPlaylist.subList(PAGE_SIZE*index, PAGE_SIZE*index + max);
                break;
            } catch (Exception e) {
                max--;
            } finally {
                if(max == 0)
                    break;
            }
        }

        return ret;
    }




    public void addSongToPlaylist(int sessionID, String playlistName, String songID) throws Exception {
        MusicClass musicClass = MusicDatabase.GetInstance().getSongByID(songID);
        ProfileAccount profileAccount = getProfile(sessionID);
        Playlist musicClassList = profileAccount.getPlaylist(playlistName);
        for(MusicClass musicClass1 : musicClassList.getMusicClassList()) {
            if(musicClass1.getSongID().equals(songID))
                return;
        }
        profileAccount.addToPlaylist(playlistName, musicClass);
        sync(getProfile(sessionID), profileAccount);
    }

    public void removeSongFromPlaylist(int sessionID, String playlistName, String songID) throws Exception {
        ProfileAccount profileAccount = getProfile(sessionID);
        profileAccount.removeFromPlaylist(playlistName, songID);
        sync(getProfile(sessionID), profileAccount);
    }

    public void createPlaylist(int sessionID, String playlistName, ObservableList<MusicClass> list) throws Exception {
        ProfileAccount profileAccount = getProfile(sessionID);
        if(!containsUsername(profileAccount.getUsername())) return;
        for(Playlist playlist : profileAccount.getPlaylists())
            if(playlist.getName().equals(playlistName)) return;
        profileAccount.addNewPlaylist(playlistName, list);
        sync(getProfile(sessionID), profileAccount);
    }

    public List<Playlist> getPlaylistNames(int session) throws Exception {
        ProfileAccount profileAccount = getProfile(session);
        List<Playlist> ret = new ArrayList<>();
        for(Playlist playlist : profileAccount.getPlaylists()) {
            ret.add(new Playlist(playlist.getName(), new ArrayList<>()));
        }
        return ret;
    }

    public void setDfs(DFS dfs) {
        this.dfs = dfs;
    }
}
