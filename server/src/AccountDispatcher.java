import com.google.gson.Gson;
import model.ProfileAccount;
import model.SingletonProfile;
import model.SingletonProfiles;

public class AccountDispatcher {
    private SingletonProfiles profiles;

    public AccountDispatcher(){
        profiles = SingletonProfiles.GetInstance();
    }

    public String loginAccount( String username, String password ){
        if( profiles.verify( username, password ) ){
            ProfileAccount account = new ProfileAccount( username, password, profiles.getPlaylist(username) );
            Gson gson = new Gson();
            return gson.toJson( account );
        }else{
            return null;
        }
    }

    public String registerAccount( String username, String password ){
        if( profiles.contains(username) ){
            return null;
        }else{
            ProfileAccount account = new ProfileAccount(username, password);
            profiles.addProfile( account );
            Gson gson = new Gson();
            return gson.toJson( account );
        }
    }
}
