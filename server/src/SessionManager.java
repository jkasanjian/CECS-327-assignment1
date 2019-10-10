import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static Map<String,String> sessionMap;
    private static int sessionID;

    public SessionManager(){
        sessionMap = new HashMap<>();
        sessionID = 0;
    }

    public int getSessionID(){
        return sessionID++;
    }

    public void addSession( String sessionID, String username ){
        sessionMap.put( sessionID, username );
    }

    public void removeSession( String sessionID ){
        sessionMap.remove( sessionID );
    }
}
