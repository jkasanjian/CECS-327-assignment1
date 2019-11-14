package rpc;

import com.google.gson.Gson;
import model.Playlist;
import model.ProfileAccount;
import model.ProfileDatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class AccountDispatcher {
    private ProfileDatabase profiles;
    private SessionManager manager;

    public AccountDispatcher() {
        profiles = ProfileDatabase.GetInstance();
        manager  = new SessionManager();
    }

    public String logIn( String username, String password ) throws Exception {
        ProfileAccount account;
        int session = 0;
        if( profiles.verifyLogin( username, password ) ){
            session = manager.getSessionID(username);
            account = new ProfileAccount( username, password, session);
            account.setSessionID( Integer.toString( session) );
            List<Playlist> list = ProfileDatabase.GetInstance().getPlaylistNames(session);
            account.setPlaylists(list);
        }else{
            account = new ProfileAccount();
        }
        Gson gson = new Gson();
        return gson.toJson(account);
    }

    public String registerAccount( String username, String password ) throws Exception {
        if( profiles.containsUsername(username) ){
            Gson gson = new Gson();
            return gson.toJson( new ProfileAccount() );
        }else{
            ProfileAccount account = new ProfileAccount(username, password);
            profiles.addProfile( account );
            Gson gson = new Gson();
            System.out.println(gson.toJson(account));
            return gson.toJson( account );
        }
    }
}
