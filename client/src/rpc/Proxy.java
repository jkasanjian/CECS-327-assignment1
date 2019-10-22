/**
* The Proxy implements ProxyInterface class. The class is incomplete 
* 
* @author  Oscar Morales-Ponce
* @version 0.15
* @since   2019-01-24 
*/
package rpc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class Proxy implements ProxyInterface {
    private CommunicationModule communicationModule;   // This is only for test. it should use the Communication  Module

    private static Proxy proxy = null;

    public static Proxy GetInstance() {
        if(proxy == null) {
            proxy = new Proxy();
        }
        return proxy;
    }

    public void init(CommunicationModule communicationModule) {
        this.communicationModule = communicationModule;
    }

    private Proxy() {}

    private Proxy(CommunicationModule communicationModule)
    {
        this.communicationModule = communicationModule;
    }



    /*
    * Executes the  remote method "remoteMethod". The method blocks until
    * it receives the reply of the message. 
    */
    public JsonObject synchExecution(String remoteMethod, String[] param) throws IOException {
        JsonObject jsonRequest = packRequest(remoteMethod, param);

        JsonParser parser = new JsonParser();
        String strRet =  this.communicationModule.syncSend(jsonRequest.toString());
        return parser.parse(strRet).getAsJsonObject();
    }

    /*
     * Executes the  remote method remoteMethod and returns without waiting
     * for the reply. It does similar to synchExecution but does not
     * return any value
     *
     */
    public void asynchExecution(String remoteMethod, String[] param) throws IOException{
        JsonObject jsonRequest = packRequest(remoteMethod, param);
        this.communicationModule.asyncSend(jsonRequest.toString());
    }


    /**
     * Builds a JSON object for a request
     * Used byt both synchExecution(..) and asynchExecution(..)
     * @param remoteMethod String - name of remote method to be executed
     * @param param String[] array of parameters
     * @return JsonObject with correct properties from catalog
     * @throws IOException if catalog.json is not found
     */

    public static JsonObject packRequest(String remoteMethod, String[] param) throws IOException {
        JsonObject jsonRequest = new JsonObject();
        JsonObject jsonParam = new JsonObject();

        Catalog remoteMethodObj = getCatalogMethod(remoteMethod);

        jsonRequest.addProperty("remoteMethod", remoteMethodObj.getRemoteMethod());
        jsonRequest.addProperty("objectName", remoteMethodObj.getObjectName());

        ArrayList<Param> paramList = remoteMethodObj.getParams();

        for(int i = 0; i < paramList.size(); i++){
            Param p = paramList.get(i);

            if(p.getType().equals("String")){
                jsonParam.addProperty(p.getName(), param[i]);
            }
            else if(p.getType().equals("int")){
                jsonParam.addProperty(p.getName(), Integer.parseInt(param[i]));
            }

            else if(p.getType().equals("Long")){
                jsonParam.addProperty(p.getName(), Long.parseLong(param[i]));
            }
        }

        jsonRequest.add("param", jsonParam);
        jsonRequest.addProperty("call_semantics", remoteMethodObj.getCall_semantic());

        return jsonRequest;
    }


    /**
     * Reads catalog.json and gets the desired Catalog object
     * Will never return null because remoteMethod is in catalog.json
     * @param remoteMethod String - name of remote method to be executed
     * @return Catalog object with given name
     */
    public static Catalog getCatalogMethod(String remoteMethod){
        ArrayList<Catalog> catalogList = readCatalog();
        for(Catalog c : catalogList){
            if(c.getRemoteMethod().equals(remoteMethod)){
                return c;
            }
        }
        return null;
    }


    public static ArrayList<Catalog> readCatalog(){
        Gson gson = new Gson();
        String fileName = "client/catalog.json";// TODO: make relative path

        try{
            Type catalogType = new TypeToken<ArrayList<Catalog>>(){}.getType();
            ArrayList<Catalog> catalog = gson.fromJson(new FileReader(fileName), catalogType);
            return catalog;
        }

        catch(FileNotFoundException e){
            System.out.println("Catalog file not found.");
        }
        return null;
    }

}


