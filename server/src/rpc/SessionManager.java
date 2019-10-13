package rpc;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    public static Map<Integer, String> sessionMap;

    public SessionManager(){
        SessionManager.sessionMap = new HashMap<>();
    }

    public int getSessionID( String username ){
        String time = java.time.LocalTime.now().toString();
        int hash = (username+time).hashCode();
        if ( SessionManager.sessionMap.containsKey(hash) ){
            if( SessionManager.sessionMap.get(hash).equals(username) ){
                return hash;
            }
        }else{
            SessionManager.sessionMap.put(hash, username);
        }
        return hash;
    }

    public String getActiveUsername(int sessionID){
        if( SessionManager.sessionMap.containsKey(sessionID) ){
            return SessionManager.sessionMap.get(sessionID);
        }
            return null;
    }

    public void removeSession( String sessionID ){
        SessionManager.sessionMap.remove( sessionID );
    }
}
