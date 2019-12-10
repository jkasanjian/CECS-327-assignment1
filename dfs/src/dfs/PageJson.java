package dfs;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class PageJson implements Serializable
{
    ArrayList<Long> guids;
    long size;
    String creationTS;
    String readTS;
    String writeTS;
    int referenceCount;
    public PageJson()
    {
        creationTS = String.valueOf(new Date().getTime());
        writeTS = String.valueOf(new Date().getTime());
        referenceCount = 0;
    }
    // getters
    public ArrayList<Long> getGuids(){
        return this.guids;
    }
    public long getSize(){
        return this.size;
    }
    public String getCreationTS(){
        return this.creationTS;
    }
    public String getReadTS(){
        return this.readTS;
    }
    public String getWriteTS(){
        return this.writeTS;
    }
    public int getReferenceCount(){
        return this.referenceCount;
    }
    // setters
    public void setGuids(ArrayList<Long> guids){
        this.guids = guids;
    }
    public void setSize(long size){
        this.size = size;
    }
    public void setCreationTS(String creationTS){
        this.creationTS = creationTS;
    }
    public void setReadTS(String readTS){
        this.readTS = readTS;
    }
    public void setWriteTS(String writeTS){
        this.writeTS = writeTS;
    }
    public void setReferenceCount(int referenceCount){
            this.referenceCount = referenceCount;
    }


};