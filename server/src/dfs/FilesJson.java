package dfs;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilesJson implements Serializable
{
    List<FileJson> file;
    public FilesJson()
    {
        file = new ArrayList<FileJson>();
    }
    // getters
    public List<FileJson> getFile(){
        return this.file;
    }
    // setters
    public void setFile(List<FileJson> file){
        this.file = file;
    }
    // Adding a file to list
    public void addFile(FileJson file) {(this.file).add(file);}

    public ArrayList<Long> getGUIDs(String fileName){
        ArrayList<Long> ret = new ArrayList<>();
        for (FileJson f : this.file){
            if(f.name.equals(fileName)){
                for(PageJson p : f.pages){
                    ret.add(p.guid);
                }
            }
        }
        return ret;
    }

    public void deleteFile(String fileName){
        ArrayList<Long> ret = new ArrayList<>();
        for (FileJson f : this.file){
            if(f.name.equals(fileName)){
                file.remove(f);
            }
        }
    }


};

    