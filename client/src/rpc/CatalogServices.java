package rpc;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class CatalogServices {

    private static Map<String, String[]> remoteObjects;

    public static void init() {
        remoteObjects = new HashMap<>();
        remoteObjects.put("loginAccount", new String[]{"LoginServices", "username", "password"});
        remoteObjects.put("registerAccount", new String[]{"LoginServices", "username", "password"});
    }

    public static JsonObject getRemoteReference(String remoteMethod, String[] params) {
        if(!verifyMethod(remoteMethod, params))
            return null;
        JsonObject jsonRequest = new JsonObject();
        JsonObject jsonParam = new JsonObject();

        jsonRequest.addProperty("remoteMethod", remoteMethod);
        jsonRequest.addProperty("objectName", remoteObjects.get(remoteMethod)[0]);

        for(int i = 0; i < params.length; i++) {
            jsonParam.addProperty(remoteObjects.get(remoteMethod)[i+1], params[i]);
        }

        jsonRequest.add("param", jsonParam);

        return jsonRequest;
    }

    private static boolean verifyMethod(String remoteMethod, String[] params) {
        if(!remoteObjects.containsKey(remoteMethod)) return false;
        return remoteObjects.get(remoteMethod).length == params.length+1;
    }
}
