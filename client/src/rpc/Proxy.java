package rpc; /**
* The rpc.Proxy implements rpc.ProxyInterface class. The class is incomplete
* 
* @author  Oscar Morales-Ponce
* @version 0.15
* @since   2019-01-24 
*/

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;


public class Proxy implements ProxyInterface {

    private CommunicationModule communicationModule;

    public Proxy(CommunicationModule communicationModule)
    {
        this.communicationModule = communicationModule;
        CatalogServices.init();
    }

    public JsonObject synchExecution(String remoteMethod, String[] param) throws Exception {
        JsonObject jsonRequest = CatalogServices.getRemoteReference(remoteMethod, param);
        if(jsonRequest == null) {
            throw new Exception("Remote Method: " + remoteMethod + ", params: " + param.toString() + " not found\n");
        }
        String strRet =  this.communicationModule.syncSend(jsonRequest.toString());
        JsonParser parser = new JsonParser();
        return parser.parse(strRet).getAsJsonObject();
    }

    public void asynchExecution(String remoteMethod, String[] param) throws Exception
    {
        JsonObject jsonRequest = CatalogServices.getRemoteReference(remoteMethod, param);
        if(jsonRequest == null) {
            throw new Exception("Remote Method: " + remoteMethod + ", params: " + param.toString() + " not found\n");
        }
        this.communicationModule.asyncSend(jsonRequest.toString());
    }
}


