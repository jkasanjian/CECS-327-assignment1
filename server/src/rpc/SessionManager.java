package rpc;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static Map<Integer, String> sessionMap = new HashMap<>();

    public SessionManager(){}

    public int getSessionID( String username ){
        String time = java.time.LocalTime.now().toString();
        int hash = (username+time).hashCode();
        if ( sessionMap.containsKey(hash) ){
            if( sessionMap.get(hash).equals(username) ){
                return hash;
            }
        }else{
            sessionMap.put(hash, username);
        }
        return hash;
    }

    public String getActiveUsername(int sessionID){
        if( sessionMap.containsKey(sessionID) ){
            return sessionMap.get(sessionID);
        }
            return null;
    }

    public void removeSession( String sessionID ){
        sessionMap.remove( sessionID );
    }
}
