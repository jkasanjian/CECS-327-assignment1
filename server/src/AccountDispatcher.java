import com.google.gson.Gson;
import model.ProfileAccount;
import model.SingletonProfiles;

public class AccountDispatcher {
    private SingletonProfiles profiles;
    private SessionManager manager;

    public AccountDispatcher() {
        profiles = SingletonProfiles.GetInstance();
        manager  = new SessionManager();
    }

    public String logIn( String username, String password ){
        ProfileAccount account;

        if( profiles.verify( username, password ) ){
            int session = manager.getSessionID();
            manager.addSession( Integer.toString(session), username );
            account = new ProfileAccount( username, password, session, profiles.getPlaylist(username) );
        }else{
            account = new ProfileAccount();
        }

        Gson gson = new Gson();
        return gson.toJson(account);
    }

    public String registerAccount( String username, String password ){
        System.out.println(username + " " + password);
        if( profiles.contains(username) ){
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
