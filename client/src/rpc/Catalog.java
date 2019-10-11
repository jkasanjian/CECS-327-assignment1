package rpc;


import java.util.ArrayList;


public class Catalog {

    private String remoteMethod;
    private String objectName;
    private ArrayList<Param> params;
    private String call_semantic;
    private String returnType;


    public Catalog(String remoteMethod, String objectName, ArrayList<Param> params, String call_semantic, String returnType) {
        this.remoteMethod = remoteMethod;
        this.objectName = objectName;
        this.params = params;
        this.call_semantic = call_semantic;
        this.returnType = returnType;
    }

    public void setRemoteMethod(String remoteMethod) {
        this.remoteMethod = remoteMethod;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }


    public void setCall_semantic(String call_semantic) {
        this.call_semantic = call_semantic;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getRemoteMethod() {
        return remoteMethod;
    }

    public String getObjectName() {
        return objectName;
    }

    public ArrayList<Param> getParams() {
        return params;
    }

    public void setParams(ArrayList<Param> params) {
        this.params = params;
    }

    public String getCall_semantic() {
        return call_semantic;
    }

    public String getReturnType() {
        return returnType;
    }

}
