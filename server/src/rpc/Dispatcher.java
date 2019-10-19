package rpc; /**
* The rpc.Dispatcher implements rpc.DispatcherInterface.
*
* @author  Oscar Morales-Ponce
* @version 0.15
* @since   02-11-2019 
*/

import java.util.HashMap;
import java.util.*;
import java.lang.reflect.*;

import com.google.gson.*;
import model.ProfileAccount;


public class Dispatcher extends Thread implements DispatcherInterface {
    public HashMap<String, Object> ListOfObjects;
    public HashMap<String, HashMap<String, String>> atMostOnce;
    
    public Dispatcher()
    {
        ListOfObjects = new HashMap<String, Object>();
        atMostOnce    = new HashMap<>();
    }
    
    /* 
    * dispatch: Executes the remote method in the corresponding Object
    * @param request: Request: it is a Json file
    {
        "remoteMethod":"getSongChunk",
        "objectName":"SongServices",
        "param":
          {
              "song":490183,
              "fragment":2
          }
    }
    */
    public String dispatch(String request)
    {
        JsonObject jsonReturn = new JsonObject();
        JsonParser parser = new JsonParser();
        JsonObject jsonRequest = parser.parse(request).getAsJsonObject();


        JsonObject allParams = jsonRequest.get("param").getAsJsonObject();
        String firstParam ="";
        for (Map.Entry<String, JsonElement>  entry  :  allParams.entrySet())
        {
            firstParam = entry.getKey();    // either "username" or "sessionID"
            break;
        }
        System.out.println("first parameter: " +  firstParam);
        System.out.println(allParams);


        try {   // quotes included in "At-most-once"
            if( jsonRequest.get("call_semantics").toString().equals("\"At-most-once\"")) {
                // if request uses username
                if (firstParam.equals("username")) {
                    // if username not in map
                    if (!atMostOnce.containsKey(allParams.get("username").toString())){
                        // add username to map
                        atMostOnce.put(allParams.get("username").toString(), new HashMap<>());
                        // put in request and response as null
                        atMostOnce.get(allParams.get("username").toString()).put(jsonRequest.toString(), null);
                    }
                    // if request in map
                    else if (atMostOnce.get(allParams.get("username").toString()).containsKey(jsonRequest.toString())) {
                        System.out.println("Request already in map");
                        return atMostOnce.get(allParams.get("username").toString()).get(jsonRequest.toString());
                    } else {
                        // username in map, put request and response as null
                        atMostOnce.get(allParams.get("username").toString()).put(jsonRequest.toString(), null);
                    }
                    // if request uses sessionID
                } else if (firstParam.equals("sessionID")) {
                    // if sessionID not in map
                    if (!atMostOnce.containsKey(allParams.get("sessionID").toString())){
                        // add sessionID to map
                        atMostOnce.put(allParams.get("sessionID").toString(), new HashMap<>());
                        // put request in and response as null
                        atMostOnce.get(allParams.get("sessionID").toString()).put(jsonRequest.toString(), null);
                    }
                    // if request in map
                    else if (atMostOnce.get(allParams.get("sessionID").toString()).containsKey(jsonRequest.toString())) {
                        System.out.println("Request already in map");
                        return atMostOnce.get(allParams.get("sessionID").toString()).get(jsonRequest.toString());
                    } else {
                        // sessionID in map, put request and response as null
                        atMostOnce.get(allParams.get("sessionID").toString()).put(jsonRequest.toString(), null);
                    }
                }
            }


            // Obtains the object pointing to SongServices
            Object object = ListOfObjects.get(jsonRequest.get("objectName").getAsString());
            Method[] methods = object.getClass().getMethods();
            Method method = null;
            // Obtains the method
            for (int i=0; i<methods.length; i++)
            {   
                if (methods[i].getName().equals(jsonRequest.get("remoteMethod").getAsString()))
                    method = methods[i];
            }
            if (method == null)
            {
                jsonReturn.addProperty("error", "Method does not exist");
                return jsonReturn.toString();
            }
            // Prepare the  parameters 
            Class[] types =  method.getParameterTypes();
            Object[] parameter = new Object[types.length];
            String[] strParam = new String[types.length];
            JsonObject jsonParam = jsonRequest.get("param").getAsJsonObject();
            int j = 0;
            for (Map.Entry<String, JsonElement>  entry  :  jsonParam.entrySet())
            {
                strParam[j++] = entry.getValue().getAsString();
            }
            // Prepare parameters
            for (int i=0; i<types.length; i++)
            {
                switch (types[i].getCanonicalName())
                {
                    case "java.lang.Long":
                        parameter[i] =  Long.parseLong(strParam[i]);
                        break;
                    case "int":
                        parameter[i] =  Integer.parseInt(strParam[i]);
                        break;
                    case "java.lang.Integer":
                        parameter[i] =  Integer.parseInt(strParam[i]);
                        break;
                    case "java.lang.String":
                        parameter[i] = new String(strParam[i]);
                        break;
                }
            }
            // Prepare the return
            Class returnType = method.getReturnType();
            String ret = "";
            switch (returnType.getCanonicalName())
                {
                    case "java.lang.Long":
                        ret = method.invoke(object, parameter).toString();
                        break;
                    case "int":
                        ret = method.invoke(object, parameter).toString();
                        break;
                    case "java.lang.String":
                        ret = (String)method.invoke(object, parameter);
                        break;
                }

            Gson gson = new GsonBuilder().create();

            JsonObject obj;
            try {
                obj = parser.parse(ret).getAsJsonObject();
                jsonReturn.add("ret", obj);
            } catch (Exception e) {
                jsonReturn.add("ret", parser.parse(gson.toJson(new ProfileAccount(ret,""))));
            }


            // adding responses to hash map
            if( jsonRequest.get("call_semantics").toString().equals("\"At-most-once\"")){
                if(firstParam.equals("username")){
                    atMostOnce.get(allParams.get("username").toString()).put( jsonRequest.toString(), jsonReturn.toString() );
                } else if(firstParam.equals("sessionID")){
                    atMostOnce.get(allParams.get("sessionID").toString()).put( jsonRequest.toString(), jsonReturn.toString() );
                }


            }
        } catch (InvocationTargetException | IllegalAccessException e)
        {
        //    System.out.println(e);
            jsonReturn.addProperty("error", "Error on " + jsonRequest.get("objectName").getAsString() + "." + jsonRequest.get("remoteMethod").getAsString());
            e.printStackTrace();
        }
     
        return jsonReturn.toString();
    }

    public void run(){

    }

    /* 
    * registerObject: It register the objects that handle the request
    * @param remoteMethod: It is the name of the method that 
    *  objectName implements. 
    * @objectName: It is the main class that contaions the remote methods
    * each object can contain several remote methods
    */
    public void registerObject(Object remoteMethod, String objectName)
    {
        ListOfObjects.put(objectName, remoteMethod);
    }
    
    /*  Testing
    public static void main(String[] args) {
        // Instance of the rpc.Dispatcher
        rpc.Dispatcher dispatcher = new rpc.Dispatcher();
        // Instance of the services that te dispatcher can handle
        rpc.SongDispatcher songDispatcher = new rpc.SongDispatcher();
        
        dispatcher.registerObject(songDispatcher, "SongServices");  
    
        // Testing  the dispatcher function
        // First we read the request. In the f2inal implementation the jsonRequest
        // is obtained from the communication module
        try {
            String jsonRequest = new String(Files.readAllBytes(Paths.get("./getSongChunk.json")));
            String ret = dispatcher.dispatch(jsonRequest);
            System.out.println(ret);

            //System.out.println(jsonRequest);
        } catch (Exception e)
        {
            System.out.println(e);
        }
        
    }*/
}
