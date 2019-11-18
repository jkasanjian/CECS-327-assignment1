package dfs;
import java.util.ArrayList;
import java.util.List;

public class FilesJson 
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
};

    